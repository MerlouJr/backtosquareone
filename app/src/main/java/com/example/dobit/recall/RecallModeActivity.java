package com.example.dobit.recall;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.microsoft.bing.speech.SpeechClientStatus;
import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

import java.io.InputStream;
import java.util.Locale;

public class RecallModeActivity extends AppCompatActivity implements ISpeechRecognitionServerEvents, TextToSpeech.OnInitListener {

    TextToSpeech textToSpeech;
    EditText _logText;
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;
    int m_waitSeconds = 0;
    MicrophoneRecognitionClient micClient = null;
    int responded1 = 0;
    int responded2 = 0;
    int responded3 = 0;


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Locale locale = textToSpeech.getLanguage();
            int result = textToSpeech.setLanguage(locale);


            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This language is not supported");

            } else {

            }
        }else {
            Log.e("TTS", "Initialization failed");
        }
    }


    public enum FinalResponseStatus {NotReceived, OK, Timeout}

    public String getPrimaryKey() {
        return this.getString(R.string.primaryKey);
    }

    private SpeechRecognitionMode getMode() {

        return SpeechRecognitionMode.LongDictation;
    }

    private String getDefaultLocale() {
        return "en-us";
    }

    private String getAuthenticationUri() {
        return this.getString(R.string.authenticationUri);
    }

    /**
     * Writes the line.
     */
    private void WriteLine() {
        this.WriteLine("");
    }

    /**
     * Writes the line.
     * @param text The line to write.
     */
    private void WriteLine(String text) {
        this._logText.append(text + "\n");
    }


    private class RecognitionTask extends AsyncTask<Void, Void, Void> {
        DataRecognitionClient dataClient;
        SpeechRecognitionMode recoMode;
        String filename;

        RecognitionTask(DataRecognitionClient dataClient, SpeechRecognitionMode recoMode, String filename) {
            this.dataClient = dataClient;
            this.recoMode = recoMode;
            this.filename = filename;
        }



        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Note for wave files, we can just send data from the file right to the server.
                // In the case you are not an audio file in wave format, and instead you have just
                // raw data (for example audio coming over bluetooth), then before sending up any
                // audio data, you must first send up an SpeechAudioFormat descriptor to describe
                // the layout and format of your raw audio data via DataRecognitionClient's sendAudioFormat() method.
                // String filename = recoMode == SpeechRecognitionMode.ShortPhrase ? "whatstheweatherlike.wav" : "batman.wav";
                InputStream fileStream = getAssets().open(filename);
                int bytesRead = 0;
                byte[] buffer = new byte[1024];

                do {
                    // Get  Audio data to send into byte buffer.
                    bytesRead = fileStream.read(buffer);

                    if (bytesRead > -1) {
                        // Send of audio data to service.
                        dataClient.sendAudio(buffer, bytesRead);
                    }
                } while (bytesRead > 0);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                dataClient.endAudio();
            }

            return null;
        }
    }

    @Override
    public void onPartialResponseReceived(final String s) {
        this.WriteLine("--- Partial result received by onPartialResponseReceived() ---");
        this.WriteLine(s);
        this.WriteLine();
    }

    @Override
    public void onFinalResponseReceived(final RecognitionResult recognitionResult) {

        int len = recognitionResult.Results.length - 1;

        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (recognitionResult.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        recognitionResult.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);

        if (null != this.micClient && ((this.getMode() == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage)) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();
        }

        if (isFinalDicationMessage) {
            this.isReceivedResponse = FinalResponseStatus.OK;
        }

        if (!isFinalDicationMessage) {
            this.WriteLine("********* Final n-BEST Results *********");
            for (int i = 0; i < recognitionResult.Results.length; i++) {
                this.WriteLine("[" + i + "]" + " Confidence=" + recognitionResult.Results[i].Confidence +
                        " Text=\"" + recognitionResult.Results[i].DisplayText + "\"");


            }
            if(recognitionResult.Results[len].DisplayText.contains("appointment") && recognitionResult.Results[len].DisplayText.contains("2 PM") && responded1 != 1) {
                textToSpeech.speak(getString(R.string.appointment2), TextToSpeech.QUEUE_FLUSH, null);
                responded1 = 1;
            }

            else if(recognitionResult.Results[len].DisplayText.contains("appointment") && responded2 != 1) {
                textToSpeech.speak(getString(R.string.need), TextToSpeech.QUEUE_FLUSH, null);
                responded2 = 1;
            }

            else if(recognitionResult.Results[len].DisplayText.contains("wallet") && responded3 != 1) {
                textToSpeech.speak(getString(R.string.wallet), TextToSpeech.QUEUE_FLUSH, null);
                responded3 = 1;
            }

            this.WriteLine();




        }
    }

    @Override
    public void onIntentReceived(final String s) {
        this.WriteLine("--- Intent received by onIntentReceived() ---");
        this.WriteLine(s);
        this.WriteLine();
    }

    @Override
    public void onError(final int i, final String s) {
        this.WriteLine("--- Error received by onError() ---");
        this.WriteLine("Error code: " + SpeechClientStatus.fromInt(i) + " " + i);
        this.WriteLine("Error text: " + s);
        this.WriteLine();
    }

    @Override
    public void onAudioEvent(boolean b) {
        this.WriteLine("--- Microphone status change received by onAudioEvent() ---");
        this.WriteLine("********* Microphone status: " + b + " *********");
        if (b) {
            this.WriteLine("Please start speaking.");
        }

        WriteLine();
        if (!b) {
            this.micClient.endMicAndRecognition();
        }
    }


    private void LogRecognitionStart() {
        String recoSource;
        recoSource = "microphone";


        this.WriteLine("\n--- Start speech recognition using " + recoSource + " with " + this.getMode() + " mode in " + this.getDefaultLocale() + " language ----\n\n");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recall_mode);

        textToSpeech = new TextToSpeech(this, this);


        this._logText = (EditText) findViewById(R.id.etMessage);
        this.m_waitSeconds = this.getMode() == SpeechRecognitionMode.ShortPhrase ? 20 : 200;
        this.LogRecognitionStart();
        this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                this,
                this.getMode(),
                this.getDefaultLocale(),
                this,
                this.getPrimaryKey());
        this.micClient.setAuthenticationUri(this.getAuthenticationUri());
        this.micClient.startMicAndRecognition();
    }
}
