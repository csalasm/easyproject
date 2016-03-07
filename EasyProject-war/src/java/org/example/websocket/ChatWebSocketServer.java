/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.websocket;


import com.google.gson.Gson;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
//import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.server.PathParam;
import org.example.model.Message;

@ApplicationScoped
@ServerEndpoint("/actions/{projectID}")
public class ChatWebSocketServer {
    //MensajeFacade mensajeFacade = lookupMensajeFacadeBean();
    
    @Inject
    private MessageSessionHandler sessionHandler;
    private int projectID;
   
    @PostConstruct
    public void afterCreate() {
        System.out.println("ChatWebSocketServer created");
    }   
    

    @OnOpen
        public void open(@PathParam("projectID") int projectID, Session session) {
            this.projectID = projectID;
            sessionHandler.addSession(projectID, session);
    }

    @OnClose
        public void close(Session session) {
            sessionHandler.removeSession(projectID, session);
    }

    @OnError
        public void onError(Throwable error) {
            Logger.getLogger(ChatWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
        public void handleMessage(String message, Session session) {
            try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("add".equals(jsonMessage.getString("action"))) {
                Message msg = new Message();
                msg.setMessage(jsonMessage.getString("message"));
                msg.setUserEmail(jsonMessage.getString("email"));
                msg.setUserName(jsonMessage.getString("name"));
                msg.setPhotoUrl(jsonMessage.getString("photoURL"));
                msg.setTimestamp(System.currentTimeMillis());
                sessionHandler.addMessage(projectID, msg);
                
                // Guardar en la BD
                //saveChatOnBD();
            }
        }
    }
        
       
    /*private MensajeFacade lookupMensajeFacadeBean() {
        try {
            Context c = new InitialContext();
            return (MensajeFacade) c.lookup("java:global/EasyProject/EasyProject-ejb/MensajeFacade!EasyProject.ejb.MensajeFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }*/
}    