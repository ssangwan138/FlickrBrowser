package deepaksangwan.example.flickrbrowser;


import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus  {IDLE, PROCESSING, NOR_INITIALISED, FAILED_OR_EMPTY, OK};
class GetRawData  {
    private static final String TAG = "DownloadData";
    private final OnDownloadComplete mcallBack;
    private DownloadStatus mDownloadStatus;

    interface OnDownloadComplete{
        void onDownloadComplete(String s, DownloadStatus d);
    }

    public GetRawData(OnDownloadComplete m) {
        mDownloadStatus = DownloadStatus.IDLE;
        mcallBack = m;

    }

    protected void execute(final String url) {
        Log.d(TAG, "doInBackground: starts with " + url);

           final String downloadedData;
           if(url== null){
               mDownloadStatus = DownloadStatus.NOR_INITIALISED;
               Log.d(TAG, "execute: is not null");
           }
           downloadedData = DownloadData(url);
           onPostExecute(downloadedData);
    }

    private String DownloadData(String urlPath){
        Log.d(TAG, "downloadData: started");
        HttpURLConnection  connection = null;
        BufferedReader reader = null;
        try{
           mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(urlPath);
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            int response = connection.getResponseCode();
            Log.d(TAG, "downloadData: Response code was" + response);
            StringBuilder result  = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while(null != (line=reader.readLine())){
                result.append(line).append("\n");
            }
            mDownloadStatus = DownloadStatus.OK;
            Log.d(TAG, "downloadData: ended");
            return result.toString();
        }
        catch (MalformedURLException e){
            Log.e(TAG, "downloadData: Invalid URL " + e.getMessage() );
        }
        catch (IOException e){
            Log.e(TAG, "downloadData: IO exception reading data" + e.getMessage() );
        }
        catch (SecurityException e){
            Log.e(TAG, "downloadData: Security Exception " + e.getMessage() );
        }
        finally {
            if(connection != null){
                connection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e){
                    Log.e(TAG, "downloadData: Unable to close reader" + e.getMessage() );
                }

            }
        }mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
        return null;

    }
    protected void onPostExecute(String result){
        mcallBack.onDownloadComplete(result,mDownloadStatus);
        Log.d(TAG, "onPostExecute: Parameter = " + result);
    }

};
