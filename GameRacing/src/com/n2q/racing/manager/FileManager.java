package com.n2q.racing.manager;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private ArrayList<Integer> arrayHighScores;
    private File file;

    public FileManager(){
        file = new File("HighScores.txt");
        arrayHighScores = new ArrayList<>();
    }

    public void readFile(){
        if (!file.exists()){
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String contentFile;
            while ((contentFile = br.readLine()) != null){
                arrayHighScores.add(Integer.valueOf(contentFile));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(String content){
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write((content + "\r\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getHighScores() {
        int highScores = 0;
        for (int i=0; i<arrayHighScores.size(); i++){
            if (arrayHighScores.get(i) > highScores){
                highScores = arrayHighScores.get(i);
            }
        }

        return highScores;
    }
}