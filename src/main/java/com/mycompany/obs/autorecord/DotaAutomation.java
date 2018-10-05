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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.json.JsonObject;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.json.*;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import skadistats.clarity.Clarity;
import skadistats.clarity.wire.common.proto.Demo.CDemoFileInfo;

/**
 *
 * @author sab
 */
public class DotaAutomation {

    /**
     * @param args the command line arguments
     */
    public static Session session;
    public static File demFolder;
    public static File replayFolder;
    public static Robot robot;

    public static void main(String[] args) throws IOException, AWTException, InterruptedException, DeploymentException {
        // TODO code application logic here

        // Process process = new ProcessBuilder("D:\\SteamLibrary\\steamapps\\common\\dota 2 beta\\game\\bin\\win64\\Dota2.exe", "-console").start();
        //InputStream is = process.getInputStream();
        // InputStreamReader isr = new InputStreamReader(is);
        // BufferedReader br = n/demo_gototick 10minew BufferedReader(isr);
        // testing json
        connectServer();
        robot = new Robot();
        demFolder = new File("D:\\toParse");

        replayFolder = new File("D:\\SteamLibrary\\steamapps\\common\\dota 2 beta\\game\\dota\\toParse");
        File[] listOfFiles = replayFolder.listFiles();



        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fName = listOfFiles[i].getName();
                int player = Integer.parseInt(fName.substring(0, 1));
                String sampleDemo = replayFolder + "\\" + fName;
                CDemoFileInfo info = Clarity.infoForFile(sampleDemo);
                int demoLen = Math.round(info.getPlaybackTime());

                if (i == 0) {
                    switchFocus();
                }

                String demoPlay = "playdemo toParse\\" + fName;
                System.out.println(demoPlay);
                inputCommand(demoPlay);

                //time to load demo
                TimeUnit.SECONDS.sleep(30);

                //setuphud
                setReplayHud(getOctVal(player));

                startRecording();
                System.out.println("sleeping for: " + demoLen);

                int recTime = demoLen - 200;
                TimeUnit.SECONDS.sleep(10);

                stopRecording();
            }
        }

        System.exit(0);

        switchFocus();
        TimeUnit.SECONDS.sleep(1);
        setReplayHud(1);

        String demoPlay = "playdemo replays\\";

        String sampleDemo = "C:\\Users\\sab\\4149205403.dem";
        CDemoFileInfo info = Clarity.infoForFile(sampleDemo);

        System.out.println(info.getPlaybackTime());
        //System.out.println(info);

        System.exit(0);

        //get parsed dem files and save them to the same directory as .txt files
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fName = listOfFiles[i].getName();
                System.out.println("File " + listOfFiles[i].getName());
                int len = fName.length();
                String type = fName.substring(len - 3, len);

                if (type.equals("dem")) {
                    getParsed(fName);
                }
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }

        TimeUnit.SECONDS.sleep(10); //wait for cmd to execute and save .txt filees

        listOfFiles = demFolder.listFiles();

        // parse .txt files to find kills and save it to the same directory
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String fName = listOfFiles[i].getName();
                System.out.println("File " + listOfFiles[i].getName());
                int len = fName.length();
                String type = fName.substring(len - 3, len);

                if (type.equals("txt")) {
                    saveKills(fName);

                }

            }
        }

        System.exit(0);
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
        int[] recs = {10, 20, 30};

        // loop
        for (int i = 0; i < recs.length; i++) {

            String com = "demo_gototick " + recs[i] + "min";
            System.out.println(com);
            inputCommand(com);
        }

        //close session
        session.close();

    }

    public static int getOctVal(int a) {

        int b = 0x31;

        switch (a) {

            case 1:
                b = 0x31;
                break;

            case 2:
                b = 0x32;
                break;

            case 3:
                b = 0x33;
                break;

            case 4:
                b = 0x34;
                break;
            case 5:
                b = 0x35;
                break;

            case 6:
                b = 0x36;
                break;
            case 7:
                b = 0x37;
                break;
            case 8:
                b = 0x38;
                break;

            case 9:
                b = 0x39;
                break;
            case 10:
                b = 0x30;
                break;

        }

        return b;
    }

    public static void saveKills(String file) throws FileNotFoundException, IOException {

        System.out.println(file);
        BufferedReader abc = new BufferedReader(new FileReader("D:\\toParse\\" + file));

        String newFile = "D:\\toParse\\" + "final_" + file;
        System.out.println(newFile);
        File fout = new File("D:\\toParse\\" + "final_" + file);
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        List<String> lines = new ArrayList<String>();

        String lin;
        while ((lin = abc.readLine()) != null) {
            lines.add(lin);
            //System.out.println(line);
        }
        abc.close();

        int deathcount = 0;

        for (int i = 0; i < lines.size(); i++) {
            String death = "DOTA_COMBATLOG_DEATH";
            String hero = "npc_dota_hero";
            String l = lines.get(i);
            JSONObject obj = new JSONObject(l);

            if (obj.has("time")) {

                int time = obj.getInt("time");

                String type = obj.getString("type");
                if (type.equals(death)) {

                    String target = obj.getString("targetname");
                    String attacker = obj.getString("attackername");

                    if (target.contains(hero) && attacker.contains(hero)) {
                        deathcount++;
                        bw.write(Integer.toString(time));
                        bw.newLine();
                        bw.write(attacker);
                        bw.newLine();
                        bw.write(target);
                        bw.newLine();
                        bw.newLine();
                        System.out.println(time);
                        System.out.println(attacker);
                        System.out.println(target);

                    }
                }

            }
        }

        bw.close();

        System.out.println("Total death: " + deathcount);

    }

    public static void getParsed(String file) throws MalformedURLException, ProtocolException, IOException {

        Runtime.getRuntime().
                exec("cmd /c start d:\\toParse\\parse.bat " + file);

    }

    public static void setReplayHud(int player) throws InterruptedException, AWTException, IOException {

        String com1 = "dota_hide_cursor 1";
        inputCommand("dota_spectator_mode 3");
        inputCommand(com1);

        // set networth ascending press y and then press s two times
        pressSingle(KeyEvent.VK_Y);

//        robot.keyPress(KeyEvent.VK_S);
//        robot.keyRelease(KeyEvent.VK_S);
//
//        TimeUnit.SECONDS.sleep(1);
//        click(280, 100);
//        TimeUnit.SECONDS.sleep(1);
//        click(280, 100);
//        //robot.mouseMove(280, 100);
//        //robot.mousePress(0);
//
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(570, 20);
//        click(570, 20);
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(630, 20);
//        click(630, 20);
//
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(710, 20);
//        click(710, 20);
//
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(770, 20);
//
//        click(770, 20);
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(830, 20);
//
//        click(830, 20);
//
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(1080, 20);
//
//        click(1090, 20);
//
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(1140, 20);
//
//        click(1150, 20);
//
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(1210, 20);
//        click(1210, 20);
//
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(1270, 20);
//        click(1270, 20);
//
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(1330, 20);
//
//        click(1330, 20);
//
//        TimeUnit.SECONDS.sleep(2);
//        robot.mouseMove(1840, 20);
        // hide perspective hud 
        click(1840, 20);

        // hide replay bar
        click(525, 1055);

        //select player
        pressSingle(player);

        //doubleclick on side bar
    }

    public static void pressSingle(int a) {

        robot.keyPress(a);
        robot.keyRelease(a);

    }

    public static void inputCommand(String com) throws AWTException, InterruptedException, IOException {
        openConsole();

        typeText(com);
        pressEnter();
        TimeUnit.SECONDS.sleep(1);

        exitConsole();

    }

    public static void typeText(String com) throws AWTException, InterruptedException {
        StringSelection stringSelection = new StringSelection(com);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        TimeUnit.SECONDS.sleep(1);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        TimeUnit.SECONDS.sleep(1);

        //press enter
    }

    public static void pressEnter() throws AWTException {

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public static void switchFocus() throws InterruptedException {
        robot.keyPress(KeyEvent.VK_ALT); // handle
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_TAB);
        TimeUnit.SECONDS.sleep(1);
    }

    public static void click(int x, int y) throws AWTException {
        Robot bot = new Robot();
        bot.mouseMove(x, y);
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public static void openConsole() throws InterruptedException {

        robot.keyPress(KeyEvent.VK_NUMPAD5); // handle
        robot.keyRelease(KeyEvent.VK_NUMPAD5);
        TimeUnit.SECONDS.sleep(1);
    }

    public static void exitConsole() throws InterruptedException {

        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        TimeUnit.SECONDS.sleep(1);

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

    public static void startRecording() throws IOException {
        session.getBasicRemote().sendText("{\"request-type\": \"StartRecording\", \"message-id\": \"1\"}");

    }

    public static void stopRecording() throws IOException {
        session.getBasicRemote().sendText("{\"request-type\": \"StopRecording\", \"message-id\": \"1\"}");

    }

}
