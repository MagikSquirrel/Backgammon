package com.MagikSquirrel.backgammon;

public class imgResolution {
    public int _piece;
    public int _edgelr;
    public int _edgetd;
    public int _midlr;
    public int _midtd;
    public int _divider;
    public int _misc = 5;

    imgResolution(int Width, int Height) {

        //LG G3
        if(Width == 1440 && Height == 2368){
            _piece = 68;
            _edgetd = 32;
            _edgelr = 46;
            _midtd = 3;
            _midlr = 31;
            _divider = 30;
        }


        //Nexus 4
        else if(Width == 1080 && Height == 1776) {
            _piece = 50;
            _edgetd = 30;
            _edgelr = 39;
            _midtd = 12;
            _midlr = 23;
            _divider = 30;
        }

    }
}
