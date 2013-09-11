package com.example.android070;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Android070MainActivity extends Activity implements SurfaceHolder.Callback{

	
	/**	 
	 * * Variables globales para administrar la grabaci—n y reproducci—n 	 
	 * */	
	private MediaRecorder mediaRecorder = null;	
	private MediaPlayer mediaPlayer = null;	
	/**	 
	 * * Variable que define el nombre para el archivo donde escribiremos el video a grabar	 
	 * */	
	private String fileName = null;	
	/**	 
	 * * Variable que indica cuando se est‡ grabado 	 
	 * */		
	private boolean recording = false;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android070_main);
		
		 /**         
		  * * inicializamos la variable para el nombre del archivo         
		  * */        
		fileName = Environment.getExternalStorageDirectory() + "/test.mp4";
		
		/**         
		 * * inicializamos la "superficie" donde se reproducir‡ la vista previa de la grabaci—n         
		 * * y luego el video ya grabado         
		 * */                
		SurfaceView surface = (SurfaceView)findViewById(R.id.surface);        
		SurfaceHolder holder = surface.getHolder();         
		holder.addCallback(this);        
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);                
		/**         
		 * * inicializamos los botones sobre los que vamos a trabajar su evento de click         
		 * */        
		final Button btnRec = (Button)findViewById(R.id.btnRec);        
		final Button btnStop = (Button)findViewById(R.id.btnStop);                
		final Button btnPlay = (Button)findViewById(R.id.btnPlay);                    
		/**         
		 * * Bot—n para grabar         
		 * */        
		btnRec.setOnClickListener(new OnClickListener() {			        	
			@Override        	        	
			public void onClick(View v) {
				btnRec.setEnabled(false);
				btnStop.setEnabled(true);
				btnPlay.setEnabled(false);
				recording = true;

				// Se establecen las opciones de audio y de vídeo para la grabación
		    	mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		    	mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		    	mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		    	mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		    	mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

		    	// Ruta de grabación
		    	mediaRecorder.setOutputFile(fileName);

		    	try {
					mediaRecorder.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				mediaRecorder.start();
			}
       
			});                
		/**         
		 * * Bot—n para detener         
		 * */                
		btnStop.setOnClickListener(new OnClickListener() {			        	
			@Override 
			public void onClick(View v) {
				btnRec.setEnabled(true);
				btnStop.setEnabled(false);
				btnPlay.setEnabled(true);

				if (recording){
					mediaRecorder.stop();
					mediaRecorder.reset();
					recording = false;
				}else{
					mediaPlayer.stop();
					mediaPlayer.reset();
					btnPlay.setText("Reproducir");
				}
			}
   
			});                  
		/**         
		 * * Bot—n para reproducir         
		 * */                   
		btnPlay.setOnClickListener(new OnClickListener() {			        	
			@Override        	
			public void onClick(View v) {
				btnRec.setEnabled(false);
				btnStop.setEnabled(true);
				btnPlay.setEnabled(true);

				// Si no se está reproducciendo, estaba parado o pausado
				if (!mediaPlayer.isPlaying()){

					btnPlay.setText("Pause");

					// Evento cuando se llega al final de la reproducción
					mediaPlayer.setOnCompletionListener(
							new OnCompletionListener() {

						public void onCompletion(MediaPlayer mp) {
							btnRec.setEnabled(true);
							btnStop.setEnabled(false);
							btnPlay.setEnabled(true);
							btnPlay.setText("Reproducir");
						}
					});

					// Si todavía no se ha iniciado la reproducción (no estaba pausado), se prepara el reproductor
					if (mediaPlayer.getCurrentPosition() == 0){
						try{
							mediaPlayer.setDataSource(fileName);
							mediaPlayer.prepare();
						} catch(IllegalArgumentException e) {
						} catch (IOException e) {
						}
					}

					mediaPlayer.start();
				}else{
					// El botón de play también funciona para pausar cuando el vídeo se está reproduciendo
					mediaPlayer.pause();
					btnPlay.setText("Reproducir");
				}
			}
       
			});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.android070_main, menu);
		return true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		// Inicialización del reproductor y grabador si son nulos
				if (mediaRecorder == null){
					mediaRecorder = new MediaRecorder();
					mediaRecorder.setPreviewDisplay(arg0.getSurface());
				}

				if (mediaPlayer == null){
					mediaPlayer = new MediaPlayer();
					mediaPlayer.setDisplay(arg0);
				}

		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		// Se liberan los recursos asociados con estos objetos
				mediaRecorder.release();
				mediaPlayer.release();

		
	}

}
