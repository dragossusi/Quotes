package mp.team.contentprovider.api;

import android.os.AsyncTask;
import android.widget.EditText;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Dragos on 15.05.2017.
 */

public class RQAPI {
    EditText v;
    Gson gson;
    private static final String API_HOST = "http://api.forismatic.com/api/1.0/?method=getQuote&key=457653&format=json&lang=en";
    GetQuote task;
    public RQAPI(EditText v) {
        this.v = v;
        gson = new Gson();
    }

    public void stopTask() {
        if(task!=null && task.getStatus()== AsyncTask.Status.RUNNING)
            task.cancel(true);
    }

    public void startTask() {
        if(task==null || task.getStatus() == AsyncTask.Status.FINISHED) {
            task = new GetQuote();
            task.execute(null, null, null);
        }
    }

    private class GetQuote extends AsyncTask<Void,Void,Void> {
        String text;
        boolean succes;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                text = gson.fromJson(readurl(), RandomQuote.class).quoteText;
                succes = true;
            }catch (IOException e){
                succes = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(succes)
                v.setHint(text);
        }
    }

    private String readurl() throws IOException {
        URLConnection con = new URL(API_HOST).openConnection();
        con.setConnectTimeout(10*1000);
        InputStream in = con.getInputStream();
        String res = IOUtils.toString(in);
        return res;
    }
}
