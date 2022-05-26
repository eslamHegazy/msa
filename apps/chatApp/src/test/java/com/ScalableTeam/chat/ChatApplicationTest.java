package com.ScalableTeam.chat;

import com.ScalableTeam.chat.app.MyCommand;
import com.ScalableTeam.chat.app.PrivateChat.CreateChatCommand;
import com.ScalableTeam.chat.utils.Data;
import com.ScalableTeam.chat.utils.DataPopulation;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.google.common.truth.Truth.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ChatApplication.class)
public class ChatApplicationTest {

    final static Firestore database = FirestoreClient.getFirestore();

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    public void prepareData() throws ExecutionException, InterruptedException {
        DataPopulation.populateDB();
    }

    @AfterEach
    public void clearData() throws ExecutionException, InterruptedException {
        DataPopulation.clearDB();
    }

    @Test
    public void createPrivateChat() throws ExecutionException, InterruptedException {

        Map<String, Object> data = new HashMap<>();
        data.put("users", Arrays.asList("3", "4"));

        // Add data to collection of chats
        MyCommand cmd = context.getBean(CreateChatCommand.class);
        cmd.execute(data);

        // count of documents in collection should be 2
        ApiFuture<QuerySnapshot> future = database.collection("PrivateChats").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

    }
}
