package com.atet.tvmarket.entity;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class ConfigInfo implements Cloneable,Serializable,AutoType{
    private static final String TAG = "ConfigInfo";
    private static final long serialVersionUID = 1L;
    
    public static final int MODE_NONE=0x0;
    public static final int MODE_EMULATE=0x1;
    public static final int MODE_GAMEPAD=0x2;
    public static final int MODE_KEYBOARD=0x4;
//    public static final int MODE_GAMEPAD_ONLY=0x10;
    public static final int MODE_GAMEPAD_TYPE_MASK=0xFF;
    public static final int MOUSE_ENABLE=0x1;
    
    public static final int NO_ID=-100;
    private int id = NO_ID;
    @Expose
    private String pkgName;
//    @Expose
//    private String name;
//    @Expose
//    private int verCode;
//    @Expose
//    private String verName;
    @Expose
    private int mode;
    private int runtimes;
    
    private GameConfigInfo gameConfigInfo;
    private KeyConfigInfo keyboardConfigInfo;
    private KeyConfigInfo gamepadConfigInfo;
    
    public ConfigInfo() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
        ConfigInfo obj = null;  
        try {  
            obj = (ConfigInfo) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return obj;  
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public GameConfigInfo getGameConfigInfo() {
        return gameConfigInfo;
    }

    public void setGameConfigInfo(GameConfigInfo gameConfigInfo) {
        this.gameConfigInfo = gameConfigInfo;
    }
    
    public KeyConfigInfo getKeyboardConfigInfo() {
        return keyboardConfigInfo;
    }

    public void setKeyboardConfigInfo(KeyConfigInfo keyboardConfigInfo) {
        this.keyboardConfigInfo = keyboardConfigInfo;
    }

    public KeyConfigInfo getGamepadConfigInfo() {
        return gamepadConfigInfo;
    }

    public void setGamepadConfigInfo(KeyConfigInfo gamepadConfigInfo) {
        this.gamepadConfigInfo = gamepadConfigInfo;
    }

    public int getRuntimes() {
        return runtimes;
    }

    public void setRuntimes(int runtimes) {
        this.runtimes = runtimes;
    }
    
    public boolean isModeEnable(int checkMode){
        if((this.mode & checkMode) == checkMode){
            return true;
        }
        return false;
    }
    
    public boolean isEmulateEnable(){
        return isModeEnable(MODE_EMULATE);
    }
    public boolean isKeyboardEnable(){
        return isModeEnable(MODE_KEYBOARD);
    }
    public boolean isGamepadEnable(){
        return isModeEnable(MODE_GAMEPAD);
    }
//    public boolean isGamepadOnly(){
//        return isModeEnable(MODE_GAMEPAD_ONLY);
//    }
    public boolean isExistConfig(){
        if((this.mode & MODE_GAMEPAD_TYPE_MASK) == MODE_NONE){
            return false;
        }
        return true;
    }
    
}
