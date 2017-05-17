package mp.team.contentprovider.adpater;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mp.team.contentprovider.Quote;
import mp.team.contentprovider.R;
import mp.team.contentprovider.database.QuotesModifier;

/**
 * Created by Dragos on 12.05.2017.
 */

public class QuoteRecyclerAdapter extends RecyclerView.Adapter<QuoteRecyclerAdapter.ItemHolder> {
    List<Quote> quotes;
    Activity activity;

    public QuoteRecyclerAdapter(Activity activity, List<Quote> quotes) {
        this.activity = activity;
        this.quotes = quotes;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_quote, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.quote = quotes.get(position);
        holder.textQuote.setText(holder.quote.text);
        holder.dateQuote.setText(QuotesModifier.df.format(holder.quote.date));
        holder.itemView.setOnLongClickListener(new OnLongClickDel(holder.quote));
        holder.itemView.setOnClickListener(new OnClickQuote(holder.quote));
    }

    @Override
    public int getItemCount() {
        System.out.println("asdasd " + this.quotes.size());
        return quotes.size();
    }

    public void addQuote(Quote q) {
        quotes.add(0, q);
        notifyDataSetChanged();
    }

    public void deleteQuote(Quote q) {
        quotes.remove(q);
        notifyDataSetChanged();
    }

    protected class ItemHolder extends RecyclerView.ViewHolder {
        TextView textQuote;
        TextView dateQuote;
        Quote quote;

        public ItemHolder(View itemView) {
            super(itemView);
            this.textQuote = (TextView) itemView.findViewById(R.id.text_quote);
            this.dateQuote = (TextView) itemView.findViewById(R.id.data_quote);
        }
    }

    public class OnLongClickDel implements View.OnLongClickListener {
        Quote q;

        public OnLongClickDel(Quote quote) {
            q = quote;
        }

        @Override
        public boolean onLongClick(View view) {
            AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
            alertDialog.setTitle("Sterge Citat");
            alertDialog.setMessage("Esti sigur?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Da",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            QuotesModifier.deleteQuote(activity, q);
                            deleteQuote(q);
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return false;
        }
    }
    public class OnClickQuote implements View.OnClickListener {
        Quote q;

        public OnClickQuote(Quote q) {
            this.q = q;
        }

        @Override
        public void onClick(View v) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, q.text);
            sendIntent.setType("text/plain");
            activity.startActivity(sendIntent);
        }
    }
}
