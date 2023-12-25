package com.zistrong.adventofcode2023;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Day13 {
    

    @Test
    public void part3() throws IOException {


        List<String> contents = Files.readAllLines(Path.of("./src/test/resources/2023/", "day13.input"));
        List<Node> list = new ArrayList<>();
        
        boolean flag = true;

        for(String content : contents) {
            if(content.isEmpty()) {
                flag = !flag;

                for(Node node : list) {
            
                    for(int i = 1;i < node.content.length() - 1; i++) {

                    int l = i-1;
                    int r = i;
                    int count = 0;
                    while(l >= 0 && r < node.content.length() && node.content.charAt(l) == node.content.charAt(r)) {
                        count++;
                        l--;
                        r++;
                    }
                    node.list.add(count);
                    }
                }   
                list.clear();
            }

            Node node = new Node();
            node.content = content;
            list.add(node);
        }



        
    }

    private static class Node {

        String content;

        List<Integer> list = new ArrayList<>();


    }
}
