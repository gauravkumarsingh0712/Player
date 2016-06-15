package com.musicplayer.collection.android.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.musicplayer.collection.android.R;
import com.musicplayer.collection.android.adapter.DialogBoxAdapter;
import com.musicplayer.collection.android.model.LocalModel;
import com.musicplayer.collection.android.model.SongsInfoDto;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class PlayListActivity extends BaseActivity {
    // Songs list
    private ListView listView;
    private DialogBoxAdapter dialogBoxAdapter;
    private SongsInfoDto mSongsInfoDto;
    private ArrayList<String> listNameArrayList = new ArrayList<>();
    private static ArrayList<String> songPathArrayList = new ArrayList<>();
    public int index;
    private TextView songNameTextView;
    private static ArrayList<Integer> indexPos = new ArrayList<>();
    Profile fbProfile;
    private static CallbackManager callbackManager;
    private static ShareDialog shareDialog;
    ProfileTracker profileTracker;
    private static boolean canPresentShareDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        System.out.println("PlayListActivity onCreate");

        mSongsInfoDto = SongsInfoDto.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            index = bundle.getInt("position");
        }

        initializeFacebookUtil();


        initView();
        initListener();
        initData();
    }

    public void initView() {
        listView = (ListView) findViewById(R.id.list);
        songNameTextView = (TextView) findViewById(R.id.text_view);
    }

    public void initData() {
        listNameArrayList.add("Add to PlayList");
        listNameArrayList.add("Share");
        listNameArrayList.add("Delate");


        songNameTextView.setText(mSongsInfoDto.getSongNameArray().get(index));

        dialogBoxAdapter = new DialogBoxAdapter(
                PlayListActivity.this, listNameArrayList);
        listView.setAdapter(dialogBoxAdapter);
        listView.setFastScrollAlwaysVisible(true);
        dialogBoxAdapter.notifyDataSetChanged();

    }

    public void initListener() {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                setPositionOfListItems(position);

            }
        });

    }

    private void setPositionOfListItems(int position) {
        switch (position) {
            case 0:
                String songPath = mSongsInfoDto.getSongPathArray().get(index);
                if (!mSongsInfoDto.getSongPathArray().equals(mSongsInfoDto.getAddToPlayListArray())) {
                    songPathArrayList.add(songPath);
                }
                mSongsInfoDto.setAddToPlayListArray(songPathArrayList);
                indexPos.add(index);
                mSongsInfoDto.setIndexPlayListArray(indexPos);
                Toast.makeText(this, mSongsInfoDto.getSongNameArray().get(index) + " has to be added in playlist. ", Toast.LENGTH_LONG).show();
                finish();
                break;
            case 1:

                shareVideoUrlFacebook();
                break;
            case 2:

                if (mSongsInfoDto.getSongPathArray().size() > 0) {
                    mSongsInfoDto.getSongNameArray().remove(index);
                    mSongsInfoDto.getSongImageBitmapArray().remove(index);
                    mSongsInfoDto.getSongPathArray().remove(index);

                }

                LocalModel.getInstance().getUpdateUIAdapterInterface().updateUi();

                finish();

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void getFacebookLoginStatus(final LoginResult loginResult) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            URL image_path = null;
                            try {
                                image_path = new URL("http://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large");
                                System.out.println("Image Path : " + image_path.toString());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            Log.v("LoginActivity", response.toString());


                        } catch (Exception e) {
                            LoginManager.getInstance().logOut();
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender, birthday, first_name, last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }


    public void loginWithFacebook() {
        if (Profile.getCurrentProfile() == null) {
            LoginManager.getInstance().logInWithReadPermissions(PlayListActivity.this, Collections.singletonList("public_profile, email, user_birthday"));
        }
    }

    public void shareVideoUrlFacebook() {
        try {
            final FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException error) {
                    String title = "Error";
                    String alertMessage = error.getMessage();
                    System.out.println("facebook sharing success :" + title);
                }

                @Override
                public void onSuccess(Sharer.Result result) {

                    System.out.println("facebook sharing success");

                }
            };

            FacebookSdk.sdkInitialize(getApplicationContext());

            callbackManager = CallbackManager.Factory.create();

            shareDialog = new ShareDialog(this);
            shareDialog.registerCallback(
                    callbackManager,
                    shareCallback);

            canPresentShareDialog = ShareDialog.canShow(
                    ShareLinkContent.class);

            Profile profile = Profile.getCurrentProfile();

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(mSongsInfoDto.getSongNameArray().get(index))
                    .setContentDescription("Album Name- " + mSongsInfoDto.getAlbumNameArray().get(index) + ", Artist Name- " + mSongsInfoDto.getArtistNameArray().get(index))
                    .setContentUrl(Uri.parse("https://rink.hockeyapp.net/manage/apps/322921"))
                    .build();


            if (profile != null) {
                if (canPresentShareDialog) {
                    shareDialog.show(linkContent);
                } else if (profile != null && hasPublishPermission()) {
                    ShareApi.share(linkContent, shareCallback);
                }
            } else {
                loginWithFacebook();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }


    public void initializeFacebookUtil() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        System.out.println("Facebook login successful");
                        getFacebookLoginStatus(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        showAlert();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        showAlert();
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(PlayListActivity.this)
                                .setTitle("Cancelled")
                                .setMessage("Permission not granted")
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                });

        fbProfile = Profile.getCurrentProfile();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    //shareVideoUrlFacebook();
                }
            }
        };
    }

}
