package com.example.ghosthunt;
//newest version

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;












import android.support.v4.app.FragmentActivity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener, com.google.android.gms.location.LocationListener, OnMarkerClickListener,  OnMarkerDragListener
{
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private boolean mUpdatesRequested;
	private static final int MILLISECONDS_PER_SECOND = 1000;

	public static final long UPDATE_INTERVAL_IN_SECONDS = (long).1;

	private static final long FASTEST_INTERVAL_IN_SECONDS = (long).1;

	private static final long FASTEST_INTERVAL =
			MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

	LocationRequest mLocationRequest;

	private static final NumberFormat nf = new DecimalFormat("##.########");
	public LocationClient mLocationClient;
	public boolean firstTime = true;
	private Location loc;
	private Location sebsRoom = new Location(LocationManager.GPS_PROVIDER);
	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mEditor;
	private SharedPreferences hsPrefs;
	private SharedPreferences.Editor hsEditor;
	GoogleMap map;
	public double distance;
	public double distance2;
	public static double speed = .0000018;
	public static double ghost1Speed = .0000025;
	public static double ghost2Speed = .0000018;
	public static double ghost3Speed = .0000030;
	public static double ghost4Speed = .0000020;
	private boolean bones1Visible = false;
	private boolean bones2Visible = false;
	private boolean bones3Visible = false;
	private boolean bones4Visible = false;
	private Location ghost1Location = new Location(LocationManager.GPS_PROVIDER);
	ArrayList<Marker> markers = new ArrayList<Marker>();

	private Location ghost2Location = new Location(LocationManager.GPS_PROVIDER);
	private Location ghost3Location = new Location(LocationManager.GPS_PROVIDER);
	private Location ghost4Location = new Location(LocationManager.GPS_PROVIDER);
	private Timer myTimer;

	int start = (int)System.currentTimeMillis()/1000;
	private boolean connected = false;
	public double randomDistance = (Math.random() * .00009) + .00001;
	public ArrayList<Marker> ghosts = new ArrayList<Marker>();
	public ArrayList<Marker> bones = new ArrayList<Marker>();
	private Location bonesLocation = new Location(LocationManager.GPS_PROVIDER);
	private Location bonesLocation2 = new Location(LocationManager.GPS_PROVIDER);
	private Location bonesLocation3 = new Location(LocationManager.GPS_PROVIDER);
	private Location bonesLocation4 = new Location(LocationManager.GPS_PROVIDER);
	private Location giftLocation1 = new Location(LocationManager.GPS_PROVIDER);
	private Location giftLocation2 = new Location(LocationManager.GPS_PROVIDER);
	private Location giftLocation3 = new Location(LocationManager.GPS_PROVIDER);
	private Location giftLocation4 = new Location(LocationManager.GPS_PROVIDER);
	private Location giftLocation5 = new Location(LocationManager.GPS_PROVIDER);

	private boolean firstCheck = true;
	private boolean firstCheck2 = true;
	private boolean firstCheck3 = true;
	private boolean firstCheck4 = true;


	private boolean firstBox = true;
	private boolean secondBox = true;
	private boolean thirdBox = true;
	private boolean fourthBox = true;

	private boolean repellant = false;
	private int repellantTime = 0;


	private int myScore = 0;
	private int myHealth = 5;
	int score = 0;

	private MediaPlayer KillGhost;
	private MediaPlayer KillGhost2; 
	private MediaPlayer Dead;
	private MediaPlayer Dead2;
	private MediaPlayer background;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		myTimer = new Timer();
		myTimer.schedule(new TimerTask() {			
			@Override
			public void run() {
				TimerMethod();
			}


		}, 0, 500);


		mLocationClient = new LocationClient(this, this, this);
		mPrefs = getSharedPreferences("SharedPreferences",
				Context.MODE_PRIVATE);

		mEditor = mPrefs.edit();
		
		hsPrefs = getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
		score = hsPrefs.getInt("GH", -100);
		hsEditor = hsPrefs.edit();
		hsEditor.putInt("GH", 0);
		hsEditor.commit();
		mUpdatesRequested = true;
		System.out.println("heyo? homie? please homie?");
		mLocationRequest = LocationRequest.create();

		mLocationRequest.setPriority(
				LocationRequest.PRIORITY_HIGH_ACCURACY);

		mLocationRequest.setInterval(500);
		System.out.println("interval: " + mLocationRequest.getInterval());

		mLocationRequest.setFastestInterval(500);
		System.out.println(FASTEST_INTERVAL);


		map =  ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		GoogleMapOptions options = new GoogleMapOptions();
		options.mapType(GoogleMap.MAP_TYPE_NORMAL)
		.compassEnabled(false)
		.rotateGesturesEnabled(false)
		.tiltGesturesEnabled(false);
		map.setMyLocationEnabled(true);
		KillGhost = MediaPlayer.create(MainActivity.this,R.raw.ghosthurt);
		KillGhost2= MediaPlayer.create(MainActivity.this,R.raw.ghosthurt2);
		Dead = MediaPlayer.create(MainActivity.this,R.raw.ghostlaugh);
		Dead2 = MediaPlayer.create(MainActivity.this,R.raw.ghostlaugh2);
		background = MediaPlayer.create(MainActivity.this, R.raw.background);
	}
	private void TimerMethod() {
		this.runOnUiThread(Timer_Tick);
	}
	private Runnable Timer_Tick = new Runnable() {


		public void run() {
			background.start();
			System.out.println(((int)System.currentTimeMillis()/1000) - start);
			if (connected == true)
			{
				ghostsMove();
				giftDistanceCheck();
				bonesDistanceCheck();

				if(repellant == true )
				{
					repellantTime++;
					System.out.println("time: " + repellantTime);
				}

			}

		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {


		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	protected void onStart() {
		super.onStart();


		System.out.println("hello!");
		mLocationClient.connect();


	}
	protected void onStop() {


		mLocationClient.disconnect();
		super.onStop();
	}
	@Override
	protected void onPause() {

		mUpdatesRequested = false;
		mEditor.putBoolean("KEY_UPDATES_ON", mUpdatesRequested);
		mEditor.commit();
		super.onPause();
	}
	protected void onResume() {

		mUpdatesRequested = true;
		if (mPrefs.contains("KEY_UPDATES_ON")) {
			mUpdatesRequested =
					mPrefs.getBoolean("KEY_UPDATES_ON", false);


		} else {
			mEditor.putBoolean("KEY_UPDATES_ON", false);
			mEditor.commit();
		}
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {


	}

	@Override
	public void onConnected(Bundle arg0) {
		loc = mLocationClient.getLastLocation();

		mLocationClient.requestLocationUpdates(mLocationRequest, this);

		LatLng me = new LatLng(loc.getLatitude(), loc.getLongitude());		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 19));


		map.setOnMarkerClickListener(this);
		map.setOnMarkerDragListener(this);
		if(firstTime == true)

		{

			AssetManager am = MainActivity.this.getAssets();
			try {
				for(String name: am.list("images"))
				{
					System.out.println("assets: " + name);
				}
			} catch (IOException e) {

				e.printStackTrace();
			}
			double positiveOrNegative = Math.random();

			randomDistance = (Math.random() * .00009) + .0002;

			ghost1Location.setLatitude(loc.getLatitude() + randomDistance);
			ghost1Location.setLongitude(loc.getLongitude() + randomDistance);

			randomDistance = (Math.random() * .00009) + .0002;

			ghost2Location.setLatitude(loc.getLatitude() + randomDistance);
			ghost2Location.setLongitude(loc.getLongitude() - randomDistance);

			randomDistance = (Math.random() * .00009) + .0002;

			ghost3Location.setLatitude(loc.getLatitude() - randomDistance);
			ghost3Location.setLongitude(loc.getLongitude() - randomDistance);

			randomDistance = (Math.random() * .00009) + .0002;

			ghost4Location.setLatitude(loc.getLatitude() - randomDistance);
			ghost4Location.setLongitude(loc.getLongitude() + randomDistance);

			randomDistance = (Math.random() * .00009) + .0001;

			bonesLocation.setLatitude(loc.getLatitude() + randomDistance);
			bonesLocation.setLongitude(loc.getLongitude() + randomDistance);

			randomDistance = (Math.random() * .00009) + .0001;

			bonesLocation2.setLatitude(loc.getLatitude() - randomDistance);
			bonesLocation2.setLongitude(loc.getLongitude() + randomDistance);

			randomDistance = (Math.random() * .00009) + .0001;

			bonesLocation3.setLatitude(loc.getLatitude() + randomDistance);
			bonesLocation3.setLongitude(loc.getLongitude() - randomDistance);

			randomDistance = (Math.random() * .00009) + .0001;

			bonesLocation4.setLatitude(loc.getLatitude() - randomDistance);
			bonesLocation4.setLongitude(loc.getLongitude() - randomDistance);

			giftLocation1.setLatitude(loc.getLatitude() + .0004);
			giftLocation1.setLongitude(loc.getLongitude());

			giftLocation2.setLatitude(loc.getLatitude() - .0004);
			giftLocation2.setLongitude(loc.getLongitude());

			giftLocation3.setLatitude(loc.getLatitude() + .00008);
			giftLocation3.setLongitude(loc.getLongitude());

			giftLocation4.setLatitude(loc.getLatitude() - .00008);
			giftLocation4.setLongitude(loc.getLongitude());

			giftLocation5.setLatitude(loc.getLatitude() + .0004);
			giftLocation5.setLongitude(loc.getLongitude() + .00008);


			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(ghost1Location.getLatitude(), ghost1Location.getLongitude())).title("ghost1").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(ghost2Location.getLatitude(), ghost2Location.getLongitude())).title("ghost2").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(ghost3Location.getLatitude(), ghost3Location.getLongitude())).title("ghost3").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(ghost4Location.getLatitude(), ghost4Location.getLongitude())).title("ghost4").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));

			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude() + .0004, loc.getLongitude())).title("gift1").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.gift1))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude() - .0004, loc.getLongitude())).title("gift2").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.gift2))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude() + .00008, loc.getLongitude())).title("gift3").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.gift3))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude() - .00008, loc.getLongitude())).title("gift4").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.gift4))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude() + .0004, loc.getLongitude() + .00008)).title("loot").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.loot))));

			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(ghost1Location.getLatitude(), ghost1Location.getLongitude())).title("loot1").draggable(true).visible(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.loot))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(ghost2Location.getLatitude(), ghost2Location.getLongitude())).title("loot2").draggable(true).visible(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.loot))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(ghost3Location.getLatitude(), ghost3Location.getLongitude())).title("loot3").draggable(true).visible(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.loot))));
			markers.add(map.addMarker(new MarkerOptions().position(new LatLng(ghost4Location.getLatitude(), ghost4Location.getLongitude())).title("loot4").draggable(true).visible(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.loot))));


			bones.add(map.addMarker(new MarkerOptions().position(new LatLng(bonesLocation.getLatitude(), bonesLocation.getLongitude())).title("bones1").draggable(true).visible(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.bones))));
			bones.add(map.addMarker(new MarkerOptions().position(new LatLng(bonesLocation2.getLatitude(), bonesLocation2.getLongitude())).title("bones2").draggable(true).visible(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.bones))));
			bones.add(map.addMarker(new MarkerOptions().position(new LatLng(bonesLocation3.getLatitude(), bonesLocation3.getLongitude())).title("bones3").draggable(true).visible(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.bones))));
			bones.add(map.addMarker(new MarkerOptions().position(new LatLng(bonesLocation4.getLatitude(), bonesLocation4.getLongitude())).title("bones4").draggable(true).visible(false).icon(BitmapDescriptorFactory.fromResource(R.drawable.bones))));


			firstTime = false;

			LatLng target = new LatLng(ghost1Location.getLatitude() + .00009, ghost1Location.getLongitude() + .00009);
			connected = true;


		}

		System.out.println("gimme dat size: " + markers.size());
		TextView tempview =(TextView)findViewById(R.id.textView1);
		//myScore--;
		String newScore = "Score: " + myScore;
		tempview.setText(newScore);
	}
	@Override
	public void onDisconnected() {


	}
	@Override
	public void onLocationChanged(Location arg0) {

		loc = arg0;
		System.out.println("new loc: " + loc.toString());

		distance = loc.distanceTo(ghost1Location);
		distance2 = loc.distanceTo(ghost2Location);
		double distance3 = loc.distanceTo(ghost3Location);
		double distance4 = loc.distanceTo(ghost4Location);


		if(distance < 5 && firstBox == true && markers.get(0).getTitle().equals("ghostie1Green"))
		{
			firstBox = false;

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Kill Ghost");
			alertDialogBuilder
			.setMessage("Do you want to kill the ghost?")
			.setCancelable(false)
			.setNeutralButton("Yes!",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					KillGhost.start();
					LatLng newLoc = new LatLng(loc.getLatitude() + .00009, loc.getLongitude() - .00009);
					ghost1Location.setLatitude(ghost1Location.getLatitude() + .0001);
					ghost1Location.setLongitude(ghost1Location.getLongitude() + .0001);
					markers.get(0).remove();
					markers.remove(0);
					markers.add(0, map.addMarker(new MarkerOptions().position(new LatLng(ghost1Location.getLatitude(), ghost1Location.getLongitude())).title("ghost1").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));
					TextView tempview =(TextView)findViewById(R.id.textView1);
					myScore++;
					String newScore = "Score: " + myScore;
					tempview.setText(newScore);
					firstBox = true;
					firstCheck = true;
					markers.get(9).setVisible(true);

				}
			});


			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

		}
		else if(distance < 5 && firstBox == true && markers.get(0).getTitle().equals("ghost1")) //if red ghost eats you
		{
			firstBox = false;
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Kill Ghost");
			alertDialogBuilder
			.setMessage("The ghost just ate you buddy! You lost a point.")
			.setCancelable(false)
			.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					Dead.start();
					firstBox = true;
					ghost1Location.setLatitude(ghost1Location.getLatitude() + .00012);
					ghost1Location.setLongitude(ghost1Location.getLongitude() + .00015); //relocate ghost
					markers.get(0).remove();
					markers.remove(0);
					markers.add(0, map.addMarker(new MarkerOptions().position(new LatLng(ghost1Location.getLatitude(), ghost1Location.getLongitude())).title("ghost1").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));

					TextView tempview =(TextView)findViewById(R.id.textView1);
					myScore--;
					String newScore = "Score: " + myScore;
					tempview.setText(newScore);

					myHealth--;

					if ( myHealth == 4 ) {
						ImageView health = (ImageView)findViewById(R.id.imageView5);

						health.setVisibility(4);

					}
					else if (myHealth == 3) {

						ImageView health = (ImageView)findViewById(R.id.imageView4);

						health.setVisibility(4);

					}
					else if (myHealth == 2) {

						ImageView health = (ImageView)findViewById(R.id.imageView3);

						health.setVisibility(4);
					}

					else if (myHealth == 1) {

						ImageView health = (ImageView)findViewById(R.id.imageView2);

						health.setVisibility(4);
					}
					else if (myHealth == 0) {

						ImageView health = (ImageView)findViewById(R.id.imageView1);

						health.setVisibility(4);


						Intent i = new Intent( MainActivity.this , GameOver.class);
						i.putExtra("Score", myScore );
						startActivity(i);

					}


				}
			});




			AlertDialog alertDialog = alertDialogBuilder.create();


			alertDialog.show();

		}
		if(distance2 < 5 && secondBox == true && markers.get(1).getTitle().equals("ghostie2Green"))
		{
			secondBox = false;

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Kill Ghost");
			alertDialogBuilder
			.setMessage("Do you want to kill the ghost?")
			.setCancelable(false)
			.setNeutralButton("Yes!",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					secondBox = true;
					KillGhost.start();
					ghost2Location.setLatitude(ghost2Location.getLatitude() + .00009);
					ghost2Location.setLongitude(ghost2Location.getLongitude() - .00009); //relocate ghost
					markers.get(1).remove();
					markers.remove(1);
					markers.add(1, map.addMarker(new MarkerOptions().position(new LatLng(ghost2Location.getLatitude(), ghost2Location.getLongitude())).title("ghost2").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));

				}
			});


			AlertDialog alertDialog = alertDialogBuilder.create();


			alertDialog.show();

		}
		else if(distance2 < 5 && secondBox == true && markers.get(1).getTitle().equals("ghost2"))

		{
			secondBox = false;
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Kill Ghost");
			alertDialogBuilder
			.setMessage("The ghost just ate you buddy! You lost a point.")
			.setCancelable(false)
			.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					Dead.start();
					secondBox = true;
					ghost2Location.setLatitude(ghost2Location.getLatitude() + .00015);
					ghost2Location.setLongitude(ghost2Location.getLongitude() - .00015); //relocate ghost
					markers.get(1).remove();
					markers.remove(1);
					markers.add(1, map.addMarker(new MarkerOptions().position(new LatLng(ghost2Location.getLatitude(), ghost2Location.getLongitude())).title("ghost2").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));
					firstCheck2 = true;
					markers.get(10).setVisible(true);


					TextView tempview =(TextView)findViewById(R.id.textView1);
					myScore--;
					String newScore = "Score: " + myScore;
					tempview.setText(newScore);


					myHealth--;

					if ( myHealth == 4 ) {
						ImageView health = (ImageView)findViewById(R.id.imageView5);

						health.setVisibility(4);

					}
					else if (myHealth == 3) {

						ImageView health = (ImageView)findViewById(R.id.imageView4);

						health.setVisibility(4);

					}
					else if (myHealth == 2) {

						ImageView health = (ImageView)findViewById(R.id.imageView3);

						health.setVisibility(4);
					}

					else if (myHealth == 1) {

						ImageView health = (ImageView)findViewById(R.id.imageView2);

						health.setVisibility(4);
					}
					else if (myHealth == 0) {

						ImageView health = (ImageView)findViewById(R.id.imageView1);

						health.setVisibility(4);

						Intent i = new Intent( MainActivity.this , GameOver.class);
						i.putExtra("Score", myScore );
						startActivity(i);

					}

				}
			});

			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();

		}



		if(distance3 < 5 && thirdBox == true && markers.get(2).getTitle().equals("ghostie3Green"))
		{

			thirdBox = false;
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Kill Ghost");
			alertDialogBuilder
			.setMessage("Do you want to kill the ghost?")
			.setCancelable(false)
			.setNeutralButton("Yes!",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					thirdBox = true;
					KillGhost2.start();
					LatLng newLoc = new LatLng(loc.getLatitude() + .00009, loc.getLongitude() - .00009);
					ghost3Location.setLatitude(ghost3Location.getLatitude() - .00015);
					ghost3Location.setLongitude(ghost3Location.getLongitude() + .00015);
					markers.get(2).remove();
					markers.remove(2);
					markers.add(2, map.addMarker(new MarkerOptions().position(new LatLng(ghost3Location.getLatitude(), ghost3Location.getLongitude())).title("ghost3").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));
					TextView tempview =(TextView)findViewById(R.id.textView1);
					myScore++;
					String newScore = "Score: " + myScore;
					tempview.setText(newScore);
					markers.get(11).setVisible(true);
					firstCheck3 = true;




				}
			});


			AlertDialog alertDialog = alertDialogBuilder.create();


			alertDialog.show();

		}
		else if(distance3 < 5 && thirdBox == true && markers.get(2).getTitle().equals("ghost3")) //if red ghost eats you
		{
			thirdBox = false;
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Kill Ghost");
			alertDialogBuilder
			.setMessage("The ghost just ate you buddy! You lost a point.")
			.setCancelable(false)
			.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					Dead2.start();
					thirdBox = true;
					ghost3Location.setLatitude(ghost3Location.getLatitude() - .00015);
					ghost3Location.setLongitude(ghost3Location.getLongitude() + .00015); //relocate ghost
					markers.get(2).remove();
					markers.remove(2);
					markers.add(2, map.addMarker(new MarkerOptions().position(new LatLng(ghost3Location.getLatitude(), ghost3Location.getLongitude())).title("ghost3").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));


					TextView tempview =(TextView)findViewById(R.id.textView1);
					myScore--;
					String newScore = "Score: " + myScore;
					tempview.setText(newScore);



					myHealth--;

					if ( myHealth == 4 ) {
						ImageView health = (ImageView)findViewById(R.id.imageView5);

						health.setVisibility(4);

					}
					else if (myHealth == 3) {

						ImageView health = (ImageView)findViewById(R.id.imageView4);

						health.setVisibility(4);

					}
					else if (myHealth == 2) {

						ImageView health = (ImageView)findViewById(R.id.imageView3);

						health.setVisibility(4);
					}

					else if (myHealth == 1) {

						ImageView health = (ImageView)findViewById(R.id.imageView2);

						health.setVisibility(4);
					}
					else if (myHealth == 0) {

						ImageView health = (ImageView)findViewById(R.id.imageView1);

						health.setVisibility(4);

						Intent i = new Intent( MainActivity.this , GameOver.class);
						i.putExtra("Score", myScore );
						startActivity(i);

					}


				}
			});




			AlertDialog alertDialog = alertDialogBuilder.create();


			alertDialog.show();


		}



		if(distance4 < 5 && fourthBox == true && markers.get(3).getTitle().equals("ghostie4Green"))
		{
			fourthBox = false;

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Kill Ghost");
			alertDialogBuilder
			.setMessage("Do you want to kill the ghost?")
			.setCancelable(false)
			.setNeutralButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					fourthBox = true;
					KillGhost2.start();
					LatLng newLoc = new LatLng(loc.getLatitude() + .00009, loc.getLongitude() - .00009);
					ghost4Location.setLatitude(ghost4Location.getLatitude() - .00016);
					ghost4Location.setLongitude(ghost4Location.getLongitude() - .00018);
					markers.get(3).remove();
					markers.remove(3);
					markers.add(3, map.addMarker(new MarkerOptions().position(new LatLng(ghost4Location.getLatitude(), ghost4Location.getLongitude())).title("ghost4").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));
					TextView tempview =(TextView)findViewById(R.id.textView1);
					myScore++;
					String newScore = "Score: " + myScore;
					tempview.setText(newScore);
					firstCheck4 = true;
					markers.get(12).setVisible(true);
				}
			});

			AlertDialog alertDialog = alertDialogBuilder.create();


			alertDialog.show();

		}
		else if(distance4 < 5 && fourthBox == true && markers.get(3).getTitle().equals("ghost4")) //if red ghost eats you
		{
			fourthBox = false;
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Kill Ghost");
			alertDialogBuilder
			.setMessage("The ghost just ate you buddy! You lost a point.")
			.setCancelable(false)
			.setNeutralButton("Ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

					Dead2.start();
					fourthBox = true;
					ghost4Location.setLatitude(ghost4Location.getLatitude() - .00009);
					ghost4Location.setLongitude(ghost4Location.getLongitude() - .00009); //relocate ghost
					markers.get(3).remove();
					markers.remove(3);
					markers.add(3, map.addMarker(new MarkerOptions().position(new LatLng(ghost4Location.getLatitude(), ghost4Location.getLongitude())).title("ghost4").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie2))));


					TextView tempview =(TextView)findViewById(R.id.textView1);
					myScore--;
					String newScore = "Score: " + myScore;
					tempview.setText(newScore);


					myHealth--;

					if ( myHealth == 4 ) {
						ImageView health = (ImageView)findViewById(R.id.imageView5);

						health.setVisibility(4);

					}
					else if (myHealth == 3) {

						ImageView health = (ImageView)findViewById(R.id.imageView4);

						health.setVisibility(4);

					}
					else if (myHealth == 2) {

						ImageView health = (ImageView)findViewById(R.id.imageView3);

						health.setVisibility(4);
					}

					else if (myHealth == 1) {

						ImageView health = (ImageView)findViewById(R.id.imageView2);

						health.setVisibility(4);
					}
					else if (myHealth == 0) {

							ImageView health = (ImageView)findViewById(R.id.imageView1);

							health.setVisibility(4);

						Intent i = new Intent( MainActivity.this , GameOver.class);
						i.putExtra("Score", myScore );
						startActivity(i);

					}


				}
			});




			AlertDialog alertDialog = alertDialogBuilder.create();

			alertDialog.show();

		}

	}
	private void giftDistanceCheck() {
		//check if im on a gift
		double distance5 = loc.distanceTo(giftLocation1);
		double distance6 = loc.distanceTo(giftLocation2);
		double distance7 = loc.distanceTo(giftLocation3);
		double distance8 = loc.distanceTo(giftLocation4);
		double distance9 = loc.distanceTo(giftLocation5);

		if(distance5 < 5 && bones.get(0).isVisible() == false)
		{
			giftLocation1.setLatitude(loc.getLatitude() + .00021);
			giftLocation1.setLongitude(loc.getLongitude() + .00025);
			markers.get(4).setPosition(new LatLng(giftLocation1.getLatitude(), giftLocation1.getLongitude()));

			markers.get(4).setVisible(false);

			//markers.remove(4);

			bones.get(0).setVisible(true);
		}

		if(distance6 < 5 && bones.get(1).isVisible() == false)
		{
			giftLocation2.setLatitude(loc.getLatitude() + .0003);
			giftLocation2.setLongitude(loc.getLongitude() - .0002);
			markers.get(5).setPosition(new LatLng(giftLocation2.getLatitude(), giftLocation2.getLongitude()));

			markers.get(5).setVisible(false);
			//markers.remove(5);

			bones.get(1).setVisible(true);
		}
		if(distance7 < 5 && bones.get(2).isVisible() == false)
		{
			giftLocation3.setLatitude(loc.getLatitude() - .0002);
			giftLocation3.setLongitude(loc.getLongitude() + .00025);
			markers.get(6).setPosition(new LatLng(giftLocation3.getLatitude(), giftLocation3.getLongitude()));

			markers.get(6).setVisible(false);
			//markers.remove(6);

			bones.get(2).setVisible(true);
		}
		if(distance8 < 5 && bones.get(3).isVisible() == false)
		{
			giftLocation4.setLatitude(loc.getLatitude() + .0003);
			giftLocation4.setLongitude(loc.getLongitude() - .0002);
			markers.get(7).setPosition(new LatLng(giftLocation4.getLatitude(), giftLocation4.getLongitude()));

			markers.get(7).setVisible(false);
			//markers.remove(7);

			bones.get(3).setVisible(true);
		}
		if(distance9 < 5)
		{
			giftLocation5.setLatitude(loc.getLatitude() + .00015);
			giftLocation5.setLongitude(loc.getLongitude() - .00015);
			markers.get(8).setPosition(new LatLng(giftLocation5.getLatitude(), giftLocation5.getLongitude()));

			markers.get(8).setVisible(true);
			System.out.println("diamond");
			//markers.remove(8);
			myScore = myScore + 100;	
			String newScore = "Score: " + myScore;
			TextView tempview =(TextView)findViewById(R.id.textView1);
			tempview.setText(newScore);
			if(myScore % 5 == 0)
			{
				repellant = true;
			}

		}
	}

	private void bonesDistanceCheck() {


		if(bonesLocation.distanceTo(loc) < 5 && firstCheck == true)
		{
			markers.get(0).remove();
			markers.remove(0);
			//markers.get(0).remove();
			markers.add(0, map.addMarker(new MarkerOptions().position(new LatLng(ghost1Location.getLatitude(), ghost1Location.getLongitude())).title("ghostie1Green").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie))));
			bones.get(0).setVisible(false);
			bonesLocation.setLatitude(loc.getLatitude() + .0009);
			bonesLocation.setLongitude(loc.getLongitude() + .0009);
			bones.get(0).setPosition(new LatLng(loc.getLatitude() + .0009, loc.getLongitude() + .0009));
			System.out.println("bones1");
			firstCheck = false;
			markers.get(4).setVisible(true);
		}
		if(bonesLocation2.distanceTo(loc) < 5 && firstCheck2 == true)
		{
			markers.get(1).remove();
			markers.remove(1);
			markers.add(1, map.addMarker(new MarkerOptions().position(new LatLng(ghost2Location.getLatitude(), ghost2Location.getLongitude())).title("ghostie2Green").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie))));
			//	bones.get(1).remove();
			bones.get(1).setVisible(false);
			bonesLocation2.setLatitude(loc.getLatitude() - .0003);
			bonesLocation2.setLongitude(loc.getLongitude() + .0003);
			bones.get(1).setPosition(new LatLng(loc.getLatitude() - .0003, loc.getLongitude() + .0003));
			System.out.println("bones2");
			firstCheck2 = false;
			markers.get(5).setVisible(true);

		}
		if(bonesLocation3.distanceTo(loc) < 5 && firstCheck3 == true)
		{
			markers.get(2).remove();
			markers.remove(2);
			markers.add(2, map.addMarker(new MarkerOptions().position(new LatLng(ghost3Location.getLatitude(), ghost3Location.getLongitude())).title("ghostie3Green").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie))));

			bonesLocation3.setLatitude(loc.getLatitude() + .0004);
			bonesLocation3.setLongitude(loc.getLongitude() - .0004);
			bones.get(2).setPosition(new LatLng(loc.getLatitude() + .0004, loc.getLongitude() - .0004));		
			bones.get(2).setVisible(false);
			System.out.println("bones3");
			firstCheck3 = false;
			markers.get(6).setVisible(true);

		}
		if(bonesLocation4.distanceTo(loc) < 5 && firstCheck4 == true)
		{
			markers.get(3).remove();
			markers.remove(3);
			markers.add(3, map.addMarker(new MarkerOptions().position(new LatLng(ghost4Location.getLatitude(), ghost4Location.getLongitude())).title("ghostie4Green").draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.ghostie))));
			bones.get(3).setVisible(false);
			bonesLocation4.setLatitude(loc.getLatitude() - .0009);
			bonesLocation4.setLongitude(loc.getLongitude() - .0009);
			bones.get(3).setPosition(new LatLng(loc.getLatitude() - .0009, loc.getLongitude() - .0009));		
			System.out.println("bones4");
			firstCheck4 = false;
			markers.get(7).setVisible(true);

		}
	}
	private void ghostsMove() {


		Location testLocation = new Location(LocationManager.GPS_PROVIDER);
		testLocation.setLatitude(ghost1Location.getLatitude() + speed);
		testLocation.setLongitude(ghost1Location.getLongitude() + speed);
		System.out.println("bearing: " + testLocation.bearingTo(loc));
		double bearing = testLocation.bearingTo(loc);

		if (bearing < -90 && bearing > -180)
		{
			ghost1Location.setLatitude(ghost1Location.getLatitude() - ghost1Speed);
			ghost1Location.setLongitude(ghost1Location.getLongitude() - ghost1Speed);

		}
		else if(bearing < 90 && bearing > 0)
		{
			ghost1Location.setLatitude(ghost1Location.getLatitude() + ghost1Speed);
			ghost1Location.setLongitude(ghost1Location.getLongitude() + ghost1Speed);

		}
		else if(bearing < 180 && bearing > 90)
		{
			ghost1Location.setLatitude(ghost1Location.getLatitude() - ghost1Speed);
			ghost1Location.setLongitude(ghost1Location.getLongitude() + ghost1Speed);

		}
		else if(bearing > -90 && bearing < 0)
		{
			ghost1Location.setLatitude(ghost1Location.getLatitude() + ghost1Speed);
			ghost1Location.setLongitude(ghost1Location.getLongitude() - ghost1Speed);

		}

		Location testLocation2 = new Location(LocationManager.GPS_PROVIDER);
		testLocation2.setLatitude(ghost2Location.getLatitude() + ghost2Speed);
		testLocation2.setLongitude(ghost2Location.getLongitude() + ghost2Speed);
		System.out.println("bearing: " + testLocation2.bearingTo(loc));
		double bearing2 = testLocation2.bearingTo(loc);

		if (bearing2 < -90 && bearing2 > -180)
		{
			ghost2Location.setLatitude(ghost2Location.getLatitude() - ghost2Speed);
			ghost2Location.setLongitude(ghost2Location.getLongitude() - ghost2Speed);

		}
		else if(bearing2 < 90 && bearing2 > 0)
		{
			ghost2Location.setLatitude(ghost2Location.getLatitude() + ghost2Speed);
			ghost2Location.setLongitude(ghost2Location.getLongitude() + ghost2Speed);

		}
		else if(bearing2 < 180 && bearing2 > 90)
		{
			ghost2Location.setLatitude(ghost2Location.getLatitude() - ghost2Speed);
			ghost2Location.setLongitude(ghost2Location.getLongitude() + ghost2Speed);

		}
		else if(bearing2 > -90 && bearing2 < 0)
		{
			ghost2Location.setLatitude(ghost2Location.getLatitude() + ghost2Speed);
			ghost2Location.setLongitude(ghost2Location.getLongitude() - ghost2Speed);

		}

		Location testLocation3 = new Location(LocationManager.GPS_PROVIDER);
		testLocation3.setLatitude(ghost3Location.getLatitude() + ghost3Speed);
		testLocation3.setLongitude(ghost3Location.getLongitude() + ghost3Speed);
		System.out.println("bearing: " + testLocation3.bearingTo(loc));
		double bearing3 = testLocation3.bearingTo(loc);

		if (bearing3 < -90 && bearing3 > -180)
		{
			ghost3Location.setLatitude(ghost3Location.getLatitude() - ghost3Speed);
			ghost3Location.setLongitude(ghost3Location.getLongitude() - ghost3Speed);

		}
		else if(bearing3 < 90 && bearing3 > 0)
		{
			ghost3Location.setLatitude(ghost3Location.getLatitude() + ghost3Speed);
			ghost3Location.setLongitude(ghost3Location.getLongitude() + ghost3Speed);

		}
		else if(bearing3 < 180 && bearing3 > 90)
		{
			ghost3Location.setLatitude(ghost3Location.getLatitude() - ghost3Speed);
			ghost3Location.setLongitude(ghost3Location.getLongitude() + ghost3Speed);

		}
		else if(bearing3 > -90 && bearing3 < 0)
		{
			ghost3Location.setLatitude(ghost3Location.getLatitude() + ghost3Speed);
			ghost3Location.setLongitude(ghost3Location.getLongitude() - ghost3Speed);

		}
		Location testLocation4 = new Location(LocationManager.GPS_PROVIDER);
		testLocation4.setLatitude(ghost4Location.getLatitude() + ghost4Speed);
		testLocation4.setLongitude(ghost4Location.getLongitude() + ghost4Speed);
		System.out.println("bearing: " + testLocation4.bearingTo(loc));
		double bearing4 = testLocation4.bearingTo(loc);

		if (bearing4 < -90 && bearing4 > -180)
		{
			ghost4Location.setLatitude(ghost4Location.getLatitude() - ghost4Speed);
			ghost4Location.setLongitude(ghost4Location.getLongitude() - ghost4Speed);

		}
		else if(bearing4 < 90 && bearing4 > 0)
		{
			ghost4Location.setLatitude(ghost4Location.getLatitude() + ghost4Speed);
			ghost4Location.setLongitude(ghost4Location.getLongitude() + ghost4Speed);

		}
		else if(bearing4 < 180 && bearing4 > 90)
		{
			ghost4Location.setLatitude(ghost4Location.getLatitude() - ghost4Speed);
			ghost4Location.setLongitude(ghost4Location.getLongitude() + ghost4Speed);

		}
		else if(bearing4 > -90 && bearing4 < 0)
		{
			ghost4Location.setLatitude(ghost4Location.getLatitude() + ghost4Speed);
			ghost4Location.setLongitude(ghost4Location.getLongitude() - ghost4Speed);

		}

		LatLng markersPos = new LatLng(ghost1Location.getLatitude(), ghost1Location.getLongitude());
		LatLng markersPos2 = new LatLng(ghost2Location.getLatitude(), ghost2Location.getLongitude());
		LatLng markersPos3 = new LatLng(ghost3Location.getLatitude(), ghost3Location.getLongitude());
		LatLng markersPos4 = new LatLng(ghost4Location.getLatitude(), ghost4Location.getLongitude());

		LatLng loot1Pos = new LatLng(ghost1Location.getLatitude(), ghost1Location.getLongitude());
		LatLng loot2Pos = new LatLng(ghost2Location.getLatitude(), ghost2Location.getLongitude());
		LatLng loot3Pos = new LatLng(ghost3Location.getLatitude(), ghost3Location.getLongitude());
		LatLng loot4Pos = new LatLng(ghost4Location.getLatitude(), ghost4Location.getLongitude());


		markers.get(0).setPosition(markersPos);
		markers.get(1).setPosition(markersPos2);
		markers.get(2).setPosition(markersPos3);
		markers.get(3).setPosition(markersPos4);
		if(markers.get(9).isVisible()  == false)
		{
			markers.get(9).setPosition(loot1Pos);
		}
		if(markers.get(10).isVisible() == false)
		{
			markers.get(10).setPosition(loot2Pos);
		}
		if(markers.get(11).isVisible() == false)
		{
			markers.get(11).setPosition(loot3Pos);
		}
		if(markers.get(12).isVisible() == false)
		{
			markers.get(12).setPosition(loot4Pos);
		}

		int score = hsPrefs.getInt("GH", -100);
		
		if (score < myScore) {
			hsEditor.putInt("GH", myScore);
			hsEditor.commit();
			System.out.println("YAAAAAY HIGHSCORE IS!!!" + hsPrefs.getInt("GH", -100));
			System.out.println("AND ALSOOOO IT SHOULD BE" + myScore);
		}

	}

	public Location generateRandomLocationForGhosts()
	{
		double randomDistanceGhost = (Math.random() * .00009) + .00001;

		double positiveOrNegative = Math.random();

		if(positiveOrNegative < .5)
		{
			randomDistanceGhost = randomDistanceGhost * -1;
		}

		Location temp = new Location(LocationManager.GPS_PROVIDER);
		temp.setLatitude(loc.getLatitude() + randomDistanceGhost);
		temp.setLongitude(loc.getLongitude() + randomDistanceGhost);


		return temp;
	}

	@Override
	public void onProviderDisabled(String arg0) {


	}
	@Override
	public void onProviderEnabled(String arg0) {


	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {


	}
	@Override
	public boolean onMarkerClick(Marker arg0) {

		LatLng pos = arg0.getPosition();


		Math.abs((pos.latitude - loc.getLatitude()));
		Math.abs((pos.longitude - loc.getLongitude()));

		Location markersLocation = new Location(LocationManager.GPS_PROVIDER);
		markersLocation.setLatitude(pos.latitude);
		markersLocation.setLongitude(pos.longitude);


		distance = loc.distanceTo(markersLocation);

		if(arg0.getTitle().equals("loot1") || arg0.getTitle().equals("loot2") || arg0.getTitle().equals("loot3") || arg0.getTitle().equals("loot4"))
		{
			arg0.setVisible(false);
			myScore+=10;
		}

		return true;

	}
	@Override
	public void onMarkerDrag(Marker arg0) {




		LatLng pos = arg0.getPosition();


		if(arg0.getTitle().equals("ghost1"))
		{
			ghost1Location.setLatitude(pos.latitude);
			ghost1Location.setLongitude(pos.longitude);
		}
		else if(arg0.getTitle().equals("bones1"))
		{
			bonesLocation.setLatitude(pos.latitude);
			bonesLocation.setLongitude(pos.longitude);
		}
		else if(arg0.getTitle().equals("bones2"))
		{
			bonesLocation2.setLatitude(pos.latitude);
			bonesLocation2.setLongitude(pos.longitude);
		}
		else if(arg0.getTitle().equals("bones3"))
		{
			bonesLocation3.setLatitude(pos.latitude);
			bonesLocation3.setLongitude(pos.longitude);
		}
		else if(arg0.getTitle().equals("bones4"))
		{
			bonesLocation4.setLatitude(pos.latitude);
			bonesLocation4.setLongitude(pos.longitude);
		}

		else if(arg0.getTitle().equals("gift1"))
		{
			giftLocation1.setLatitude(pos.latitude);
			giftLocation1.setLongitude(pos.longitude);
		}
		else if(arg0.getTitle().equals("gift2"))
		{
			giftLocation2.setLatitude(pos.latitude);
			giftLocation2.setLongitude(pos.longitude);
		}
		else if(arg0.getTitle().equals("gift3"))
		{
			giftLocation3.setLatitude(pos.latitude);
			giftLocation3.setLongitude(pos.longitude);
		}
		else if(arg0.getTitle().equals("gift4"))
		{
			giftLocation4.setLatitude(pos.latitude);
			giftLocation4.setLongitude(pos.longitude);
		}
		else if(arg0.getTitle().equals("loot"))
		{
			giftLocation5.setLatitude(pos.latitude);
			giftLocation5.setLongitude(pos.longitude);
		}

	}
	@Override
	public void onMarkerDragEnd(Marker arg0) {

	}
	@Override
	public void onMarkerDragStart(Marker arg0) {

	}
	public static void setspeed(double i) {

		speed = i;
		ghost1Speed = i*2;
		ghost2Speed = i*3;
		ghost3Speed = i*4;
		ghost4Speed = i*5;

	}




}