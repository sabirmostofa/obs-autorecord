/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.obs.autorecord;

/**
 *
 * @author sab
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.AWTException;
import java.awt.Robot;
import static java.awt.SystemColor.text;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import java.net.URI;
import java.util.concurrent.TimeUnit;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 *
 * @author sab
 */
public class DotaAutomation {

    /**
     * @param args the command line arguments
     */
    public static Session session;

    public static void main(String[] args) throws IOException, AWTException, InterruptedException, DeploymentException {
        // TODO code application logic here

        // Process process = new ProcessBuilder("D:\\SteamLibrary\\steamapps\\common\\dota 2 beta\\game\\bin\\win64\\Dota2.exe", "-console").start();
        //InputStream is = process.getInputStream();
        // InputStreamReader isr = new InputStreamReader(is);
        // BufferedReader br = n/demo_gototick 10minew BufferedReader(isr);
        connectServer();
        String line;

        System.out.printf("Output of running %s is:", Arrays.toString(args));

        switchFocus();
        TimeUnit.SECONDS.sleep(1);

        // TimeUnit.SECONDS.sleep(2);
        click(100, 190); //this click is important after switch

        System.out.println("after console opened");

//        while ((line = br.readLine()) != null) {
//            System.out.println(line);
//
//        }
        int[] recs = {10,20,30};

        // loop
        for (int i = 0; i < recs.length; i++) {

            String com = "demo_gototick " + recs[i] + "min";
            System.out.println(com);
            inputCommand(com);
        }
        
        //close session
        session.close();

    }

    public static void inputCommand(String com) throws AWTException, InterruptedException, IOException {
        openConsole();

        
        typeText(com);
        pressEnter();

        exitConsole();
        startRecording();
        

        TimeUnit.SECONDS.sleep(10);
        stopRecording();

    }

    public static void startRecord() throws AWTException {

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_R);

        robot.keyRelease(KeyEvent.VK_R);
        robot.keyRelease(KeyEvent.VK_CONTROL);

    }

    public static void stopRecord() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_R);
        robot.keyRelease(KeyEvent.VK_R);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public static void typeText(String com) throws AWTException {
        StringSelection stringSelection = new StringSelection(com);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        //press enter
    }

    public static void pressEnter() throws AWTException {

        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public static void switchFocus() {
        try {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_ALT);
            r.keyPress(KeyEvent.VK_TAB);
            r.keyRelease(KeyEvent.VK_ALT);
            r.keyRelease(KeyEvent.VK_TAB);
        } catch (AWTException e) {
            System.out.println("error on switch focus");
            // handle
        }
    }

    public static void click(int x, int y) throws AWTException {
        Robot bot = new Robot();
        bot.mouseMove(x, y);
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public static void openConsole() throws InterruptedException {

        try {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_SLASH);
            r.keyRelease(KeyEvent.VK_SLASH);
            TimeUnit.SECONDS.sleep(1);

        } catch (AWTException e) {
            System.out.println("error on console");
            // handle
        }
    }

    public static void exitConsole() {
        try {
            Robot r = new Robot();
            r.keyPress(KeyEvent.VK_ESCAPE);
            r.keyRelease(KeyEvent.VK_ESCAPE);

        } catch (AWTException e) {
            System.out.println("error on console");

        }

// handle
    }

    public static void connectServer() throws IOException, DeploymentException {

        final WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();

        session = webSocketContainer.connectToServer(new Endpoint() {
            @Override
            public void onOpen(Session session, EndpointConfig config) {
                // session.addMessageHandler( ... );
            }
        }, URI.create("ws://127.0.0.1:4440"));

       

    }
    
    public static void startRecording() throws IOException{
         session.getBasicRemote().sendText("{\"request-type\": \"StartRecording\", \"message-id\": \"1\"}");
    

}
    
        public static void stopRecording() throws IOException{
         session.getBasicRemote().sendText("{\"request-type\": \"StopRecording\", \"message-id\": \"1\"}");
    

}

}
