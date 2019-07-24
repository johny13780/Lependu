
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thiro
 */
public class Controler {
    private Model model;
    
    public Controler(Model model){
        this.model = model;
    }
    
    public void check(char letter){
        boolean find = false;
        
        letter = (char)((int)letter + 32);
        
        for(int i=0; i < model.getWord().length(); i++){
            if(letter == model.getWord().charAt(i)){
                model.writeAt(i, letter);
                find = true;
            }
        }
        
        if(!find){
            model.setLife(model.getLife() - 1);
            
            if(model.getLife() <= 0)
                model.notifyObservers(GameState.Lose);
            else
                model.notifyObservers(GameState.InGame);
        }
        else{
            if(checkWin()){
                model.calculScore();
                model.notifyObservers(GameState.Win);
            }
            else{
                model.notifyObservers(GameState.InGame);
            }
        }
    }

    public void newGame(int score){
        model.newGame(score);
    }
    
    public boolean checkScore(){     
        ArrayList<Score> scores = model.getListScore();

        int i= 0;
        for(Score s : scores){
            if(model.getScore() > s.getScore()){
                break;
            }
            i++;
        }
        
        if(i<10){
            return true;
        }   
        return false;
    }
    
    public void saveScore(String name){       
        ArrayList<Score> scores = model.getListScore();
        
        int i= 0;
        for(Score s : scores){
            if(model.getScore() > s.getScore()){
                break;
            }
            i++;
        }
        
        scores.add(i, new Score(name, model.getScore()));
        ObjectOutputStream oos;
        
        try{
            oos = new ObjectOutputStream(
                    new BufferedOutputStream(
                        new FileOutputStream(
                            new File("sauvegarde.txt"))));
            
            oos.writeObject(model.getData());
            
            oos.close();
        }catch(IOException ex){
           System.out.println(ex.getMessage());
        }
    }
    
    public void loadScore(){
        ObjectInputStream ois;
        
        try{
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                        new FileInputStream(
                            new File("sauvegarde.txt"))));
            
            model.setData((Data)ois.readObject());
            ois.close();
        }
        catch(ClassNotFoundException | IOException ex){
           System.out.println(ex.getMessage());
        }
    }
    
    public boolean checkWin(){
        for(int i=0; i<model.getWord().length(); i++){
            if(model.readAt(i) == '*')
                return false;
        }
        
        return true;
    }

    public void getBestScore(){
        model.notifyObservers(GameState.Score);
    }
}
