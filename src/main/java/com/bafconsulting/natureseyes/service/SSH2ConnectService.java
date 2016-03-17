package com.bafconsulting.natureseyes.service;
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2013 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright 
     notice, this list of conditions and the following disclaimer in 
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsch.examples.OpenSSHConfig.MyUserInfo;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import com.jcraft.jsch.*;

@Service
@Transactional
public class SSH2ConnectService {
	public void sshConnect() {
	    
	    try{
	        JSch jsch=new JSch();  

	        String host=null;
	        //if(arg.length>0){
	       //   host=arg[0];
	        //}
	       // else{
	          //host=JOptionPane.showInputDialog("Enter username@hostname",
	          //                                 System.getProperty("user.name")+
	          //                                 "@localhost");
	        	//default user and static ip
	        	String username = "pi";
	        	String ipaddress = "192.168.1.50";
	        	host=username+"@"+ipaddress;
	      //  }
	        String user=host.substring(0, host.indexOf('@'));
	        host=host.substring(host.indexOf('@')+1);

	        Session session=jsch.getSession(user, host, 22);
	        
	        /*
	        String xhost="127.0.0.1";
	        int xport=0;
	        String display=JOptionPane.showInputDialog("Enter display name", 
	                                                   xhost+":"+xport);
	        xhost=display.substring(0, display.indexOf(':'));
	        xport=Integer.parseInt(display.substring(display.indexOf(':')+1));
	        session.setX11Host(xhost);
	        session.setX11Port(xport+6000);
	        */

	        // username and password will be given via UserInfo interface.
	        UserInfo ui=new MyUserInfo();
	        session.setUserInfo(ui);
	        session.connect();

	        //String command=JOptionPane.showInputDialog("Enter command", 
	        //                                           "set|grep SSH");

	        String command = "./RTSPCamRTMPLocal1.sh";
	        
	        Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);

	        // X Forwarding
	        // channel.setXForwarding(true);

	        //channel.setInputStream(System.in);
	        channel.setInputStream(null);

	        //channel.setOutputStream(System.out);

	        //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
	        //((ChannelExec)channel).setErrStream(fos);
	        ((ChannelExec)channel).setErrStream(System.err);

	        InputStream in=channel.getInputStream();

	        channel.connect();

	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            System.out.print(new String(tmp, 0, i));
	          }
	          if(channel.isClosed()){
	            if(in.available()>0) continue;
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        channel.disconnect();
	        session.disconnect();
	      }
	      catch(Exception e){
	        System.out.println(e);
	      }
	    }

	    public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
	      public String getPassword(){ return passwd; }
	      public boolean promptYesNo(String str){
	        Object[] options={ "yes", "no" };
	        int foo = 0;
			//int foo=JOptionPane.showOptionDialog(null, 
	        //       str,
	        //       "Warning", 
	        //       JOptionPane.DEFAULT_OPTION, 
	        //       JOptionPane.WARNING_MESSAGE,
	        //       null, options, options[0]);
	         return foo==0;
	      }
	    
	      String passwd;
	      JTextField passwordField=(JTextField)new JPasswordField(20);

	      public String getPassphrase(){ return null; }
	      public boolean promptPassphrase(String message){ return true; }
	      public boolean promptPassword(String message){
	        Object[] ob={passwordField}; 
	        //int result=
	        //  JOptionPane.showConfirmDialog(null, ob, message,
	        //                                JOptionPane.OK_CANCEL_OPTION);
	        //if(result==JOptionPane.OK_OPTION){
	          //passwd=passwordField.getText();
	        //default password
	        passwd = "raspberry";
	        return true;
	        //}
	       // else{ 
	       //   return false; 
	       // }
	      }
	      public void showMessage(String message){
	        JOptionPane.showMessageDialog(null, message);
	      }
	      final GridBagConstraints gbc = 
	        new GridBagConstraints(0,0,1,1,1,1,
	                               GridBagConstraints.NORTHWEST,
	                               GridBagConstraints.NONE,
	                               new Insets(0,0,0,0),0,0);
	      private Container panel;
	      public String[] promptKeyboardInteractive(String destination,
	                                                String name,
	                                                String instruction,
	                                                String[] prompt,
	                                                boolean[] echo){
	        panel = new JPanel();
	        panel.setLayout(new GridBagLayout());

	        gbc.weightx = 1.0;
	        gbc.gridwidth = GridBagConstraints.REMAINDER;
	        gbc.gridx = 0;
	        panel.add(new JLabel(instruction), gbc);
	        gbc.gridy++;

	        gbc.gridwidth = GridBagConstraints.RELATIVE;

	        JTextField[] texts=new JTextField[prompt.length];
	        for(int i=0; i<prompt.length; i++){
	          gbc.fill = GridBagConstraints.NONE;
	          gbc.gridx = 0;
	          gbc.weightx = 1;
	          panel.add(new JLabel(prompt[i]),gbc);

	          gbc.gridx = 1;
	          gbc.fill = GridBagConstraints.HORIZONTAL;
	          gbc.weighty = 1;
	          if(echo[i]){
	            texts[i]=new JTextField(20);
	          }
	          else{
	            texts[i]=new JPasswordField(20);
	          }
	          panel.add(texts[i], gbc);
	          gbc.gridy++;
	        }

	        if(JOptionPane.showConfirmDialog(null, panel, 
	                                         destination+": "+name,
	                                         JOptionPane.OK_CANCEL_OPTION,
	                                         JOptionPane.QUESTION_MESSAGE)
	           ==JOptionPane.OK_OPTION){
	          String[] response=new String[prompt.length];
	          for(int i=0; i<prompt.length; i++){
	            response[i]=texts[i].getText();
	          }
	  	return response;
	        }
	        else{
	          return null;  // cancel
	        }
	      }
	    }
	  }
