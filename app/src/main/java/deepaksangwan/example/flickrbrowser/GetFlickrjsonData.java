package deepaksangwan.example.flickrbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrjsonData extends Thread implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrjsonData";
    private List<Photo> mPhotoList;
    private String mBaseUrl;
    private String mLanguage;
    private boolean mMatchAll;
    private final MainActivity mainActivity;
    private boolean runningOnSameThread = false;
    private final OnDataAvailable mcallBack;
    interface OnDataAvailable{
        void onDataAvailable(List<Photo> data, DownloadStatus d);
    }

    public GetFlickrjsonData(String mBaseUrl, String mLanguage, boolean mMatchAll, OnDataAvailable mcallBack, MainActivity m) {
        Log.d(TAG, "GetFlickrjsonData: called");
        this.mBaseUrl = mBaseUrl;
        this.mLanguage = mLanguage;
        this.mMatchAll = mMatchAll;
        this.mcallBack = mcallBack;
        mainActivity = m;
    }
    void ExecuteOnSameThread(String searchCriteria){
        Log.d(TAG, "ExecuteOnSameThread: starts");
        runningOnSameThread = true;
        String destinationUri = createUri(searchCriteria,mLanguage,mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(destinationUri);
        Log.d(TAG, "ExecuteOnSameThread: ends");
    }

    protected void execute(String searchCriteria){
        Log.d(TAG, "execute: Starts");
        new Thread(()-> {
            String destinationUri = createUri(searchCriteria,mLanguage,mMatchAll);
            GetRawData getRawData = new GetRawData(this);
            getRawData.execute(destinationUri);
            if(mainActivity!=null)
                mainActivity.runOnUiThread(()->{
                postExecute(mPhotoList);
            });
        else return;}).start();
    }
    protected void postExecute(List<Photo> p){
        Log.d(TAG, "postExecute: starts");
        if(mcallBack != null){
            mcallBack.onDataAvailable(p,DownloadStatus.OK);
        }
        Log.d(TAG, "postExecute: ends ");
    }

    private String createUri(String searchCriteria, String lang, boolean matchAll){
        Log.d(TAG, "createUri: starts");
        return Uri.parse(mBaseUrl).buildUpon()
                .appendQueryParameter("tags", searchCriteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }
    @Override
    public void onDownloadComplete(String s, DownloadStatus d) {
        Log.d(TAG, "onDownloadComplete: starts");
        if(d == DownloadStatus.OK){
            mPhotoList = new ArrayList<>();
            try{
                Log.d(TAG, "onDownloadComplete: try block");
                JSONObject jsonData = new JSONObject(s);
                Log.d(TAG, "onDownloadComplete: object created");
                JSONArray itemsArray = jsonData.getJSONArray("items");
                for(int i=0;i<itemsArray.length();i++){
                    JSONObject jsonPhoto = itemsArray.getJSONObject(i);
                    String title = jsonPhoto.getString("title");
                    String author = jsonPhoto.getString("author");
                    String authorId = jsonPhoto.getString("author_id");
                    String tags = jsonPhoto.getString("tags");

                    JSONObject jsonMedia = jsonPhoto.getJSONObject("media");
                    String photoUrl = jsonMedia.getString("m");

                    String link = photoUrl.replaceFirst("_m.", "_b.");
                    Photo photoObject = new Photo(title,author,authorId,link,tags,photoUrl);
                    mPhotoList.add(photoObject);
                    Log.d(TAG, "onDownloadComplete: "+ photoObject.toString());
                }

            }
            catch (JSONException jsone){
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing json data" + jsone.getMessage() );
                d = DownloadStatus.FAILED_OR_EMPTY;
            }
        }if(runningOnSameThread && mcallBack != null){
            mcallBack.onDataAvailable(mPhotoList, d);
        }
        Log.d(TAG, "onDownloadComplete finished ");
    }
}
