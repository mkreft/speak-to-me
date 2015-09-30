package it.moondroid.chatbot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by marco.granatiero on 30/09/2014.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> implements TextToSpeech.OnInitListener{

    private static final int LEFT_MESSAGE = -1;
    private static final int RIGHT_MESSAGE = 1;

    private TextView messageTextView;
    private List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
    private LinearLayout wrapper;
    private TextToSpeech tts;

    @Override
    public void add(ChatMessage object) {
        chatMessages.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        tts = new TextToSpeech(context, this);
    }

    public int getCount() {
        return this.chatMessages.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessages.get(index);
    }

    private void speakOut(String text) {

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {


        int result = tts.setLanguage(Locale.ITALY);
            tts.setSpeechRate(0.85f);
            tts.setPitch(0.7f);

        if (result == TextToSpeech.LANG_MISSING_DATA
        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        Log.e("TTS", "This Language is not supported");
        } else {
        //btnSpeak.setEnabled(true);
        speakOut("Ciao! Sono bot Babbel. Parliamo italiano.");
        }

        } else {
        Log.e("TTS", "Initilization Failed!");
        }
        }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (chatMessages.get(position).left ? LEFT_MESSAGE : RIGHT_MESSAGE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ChatMessage comment = getItem(position);
        //if(position == getCount()){

        //}
        int type = getItemViewType(position);

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(type==LEFT_MESSAGE){
                row = inflater.inflate(R.layout.chat_listitem_left, parent, false);
                if(position == getCount()-1){
                    speakOut(comment.text);
                }

            }
            if(type==RIGHT_MESSAGE){
                row = inflater.inflate(R.layout.chat_listitem_right, parent, false);
            }
        }

        wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

        messageTextView = (TextView) row.findViewById(R.id.text);
        messageTextView.setText(comment.text);

        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
