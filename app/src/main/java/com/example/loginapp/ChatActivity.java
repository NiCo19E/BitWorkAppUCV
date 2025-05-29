package com.example.loginapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText inputReceiverEmail, inputMessage;
    private Button buttonSend;
    private TextView chatTextView;
    private ScrollView scrollView;

    private FirebaseFirestore db;
    private String currentUserEmail;
    private String chatPartnerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        inputReceiverEmail = findViewById(R.id.inputReceiverEmail);
        inputMessage = findViewById(R.id.inputMessage);
        buttonSend = findViewById(R.id.buttonSend);
        chatTextView = findViewById(R.id.chatTextView);
        scrollView = findViewById(R.id.scrollView);

        db = FirebaseFirestore.getInstance();
        currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        buttonSend.setOnClickListener(v -> {
            String receiverEmail = inputReceiverEmail.getText().toString().trim();
            String messageText = inputMessage.getText().toString().trim();

            if (TextUtils.isEmpty(receiverEmail)) {
                Toast.makeText(this, "Ingresa el correo del destinatario", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(messageText)) {
                Toast.makeText(this, "Escribe un mensaje", Toast.LENGTH_SHORT).show();
                return;
            }

            chatPartnerEmail = receiverEmail;
            sendMessage(receiverEmail, messageText);
            inputMessage.setText("");
            listenMessages();
        });
    }

    private void sendMessage(String receiverEmail, String messageText) {
        Map<String, Object> message = new HashMap<>();
        message.put("senderEmail", currentUserEmail);
        message.put("receiverEmail", receiverEmail);
        message.put("message", messageText);
        message.put("timestamp", Timestamp.now());

        db.collection("messages").add(message);
    }

    private void listenMessages() {
        if (chatPartnerEmail == null) return;

        db.collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null || querySnapshot == null) return;

                    chatTextView.setText("");
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String sender = doc.getString("senderEmail");
                        String receiver = doc.getString("receiverEmail");
                        String message = doc.getString("message");

                        if (sender == null || receiver == null || message == null) continue;

                        boolean isChatMessage =
                                (sender.equals(currentUserEmail) && receiver.equals(chatPartnerEmail)) ||
                                        (sender.equals(chatPartnerEmail) && receiver.equals(currentUserEmail));

                        if (isChatMessage) {
                            String from = sender.equals(currentUserEmail) ? "Tú" : "Otro";
                            chatTextView.append(from + ": " + message + "\n");
                        }
                    }

                    scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                });
    }
}
