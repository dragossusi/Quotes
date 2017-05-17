package mp.team.contentprovider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import mp.team.contentprovider.api.RQAPI;
import mp.team.contentprovider.database.QuotesModifier;

public class AddQuoteActivity extends AppCompatActivity {
    //variabilele classei
    int nr = 0;
    Button butonel;
    EditText editText;
    RQAPI rqapi;
    MenuItem copyquote;
    MenuItem refreshquote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //pune activitatii xml-ul pt interfata(UI)
        setContentView(R.layout.activity_add_quote);
        //ia obiectele din interfata sa lucrezi pe ele
        editText = (EditText) findViewById(R.id.edit_add);
        butonel = (Button) findViewById(R.id.button_add);

        //adauga eveniment pentru cand se apasa butonul
        butonel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Quote quote = new Quote(editText.getText().toString(), new Date());
                QuotesModifier.addQuote(AddQuoteActivity.this,quote);
                Intent intent = new Intent();
                intent.putExtra("quote",quote);
                setResult(RESULT_OK, intent);
                rqapi.stopTask();
                finish();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                System.out.println(s);
                if(s.toString().equals("")){
                    copyquote.setVisible(true);
                    refreshquote.setVisible(true);
                } else {
                    copyquote.setVisible(false);
                    refreshquote.setVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        rqapi.stopTask();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        copyquote = menu.findItem(R.id.action_check);
        refreshquote = menu.findItem(R.id.action_refresh);

        rqapi = new RQAPI(editText);
        rqapi.startTask();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            rqapi.startTask();
            return true;
        } else if (id == R.id.action_check) {
            editText.setText(editText.getHint().toString());
            copyquote.setVisible(false);
            refreshquote.setVisible(false);
        }

        return super.onOptionsItemSelected(item);
    }
}
