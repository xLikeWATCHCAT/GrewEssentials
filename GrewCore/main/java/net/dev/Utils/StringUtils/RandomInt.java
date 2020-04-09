package net.dev.Utils.StringUtils;

import java.util.*;

public class RandomInt {
    public int getRandomInt(int Max,int Min){
        Random random = new Random();
        int i = random.nextInt(Max)%(Max-Min+1) + Min;
        return i;
    }
}
