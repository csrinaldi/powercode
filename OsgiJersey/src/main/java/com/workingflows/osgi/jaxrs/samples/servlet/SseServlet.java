/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.workingflows.osgi.jaxrs.samples.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SseServlet extends HttpServlet {
 
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
 
        PrintWriter writer = response.getWriter();
        int upVote = 0;
        int downVote = 0;
        for (int i = 0; i < 200; i++) {
 
            upVote = upVote + (int) (Math.random() * 10);
            downVote = downVote + (int) (Math.random() * 10);
 
            writer.write("event:msg1\n");
            writer.write("data: " + upVote + "\n\n");
 
            writer.write("event:msg2\n");
            writer.write("data: " + downVote + "\n\n");
 
            writer.flush();
             
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writer.close();
    }
 
}
