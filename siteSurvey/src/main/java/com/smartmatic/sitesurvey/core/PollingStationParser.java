package com.smartmatic.sitesurvey.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import com.smartmatic.sitesurvey.data.*;

import android.content.Context;
import android.os.Environment;

/**
 * <p>
 *     This class parses the file containing all the Polling Stations. The PollingStations.txt file
 *     must be located in \Android\data\com.smartmatic.sitesurvey\files\ for the file to be opened.
 *     Once the file is opened, each line represents a Polling Station, the format is:
 *     "/tName(String) /t Reference(String) /tLatitude(double) /tLongitude(double)"
 *     Once these lines are parsed, they are split and assigned to a variable, then they are used to
 *     create a new Polling Station.
 * </p>
 *
 * @author Reynaldo
 */

public class PollingStationParser {

	private static final String configFile = "PollingStations.txt";

	public static ArrayList<PollingStation> getPollingStations(Context context) {
		try {
			return parse(getConfigFile(context));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static BufferedReader getConfigFile(Context context) {
		if(isExternalStorageReadable()){
			// Get the directory for the app's private files
			try {
				File file = new File(context.getExternalFilesDir(null), configFile);

				FileReader fr = new FileReader(file);

				return new BufferedReader(fr);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/* Checks if external storage is available to at least read */
	private static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	public static ArrayList<PollingStation> parse(BufferedReader reader) throws IOException {
		try {
			ArrayList<PollingStation> list = new ArrayList<PollingStation>();

			reader.readLine(); // AVOID HEADER

			String line = reader.readLine();
			while( line !=null){
				PollingStation ps = new PollingStation();
				String[] parts = line.split("\t");
				ps.id = list.size();
				ps.title = parts[1];
				ps.description=parts[2];
				ps.lat=Double.parseDouble(parts[3]);
				ps.lon=Double.parseDouble(parts[4]);

				list.add(ps);
				line = reader.readLine();
			}

			return list;
		} finally {
			reader.close();
		}
	}

}