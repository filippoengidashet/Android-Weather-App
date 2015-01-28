package com.androidweatherapp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidweatherapp.controller.Controller;
import com.androidweatherapp.model.model.Setting;

public class SettingActivity extends Activity {

	private Controller controller = null;
	private ListView cityListView = null;
	private ArrayAdapter<String> adapter = null;
	private ArrayAdapter<String> spinnerAdapter = null;
	private Button addBtn = null, resetBtn;
	private AlertDialog dialog = null;
	private EditText newCity = null;
	private CheckBox showIcons = null;
	private Setting setting = null;
	private Spinner daysSpinner = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		initialize();

		addBtn.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addCityDialog();
			}
		});

		resetBtn.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				resetDefaultDialog();
			}
		});

		cityListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String itemSelected = (String) arg0.getItemAtPosition(arg2);
				deleteCityDialog(itemSelected);

			}
		});
		if (controller.getSettings().getShowIcon().equals("true")) {
			showIcons.setChecked(true);
		} else {
			showIcons.setChecked(false);
		}

		showIcons.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				controller.updateCheckBox(Boolean.toString(isChecked));
				Toast.makeText(SettingActivity.this,
						"CheckBox val is Updated!", Toast.LENGTH_SHORT).show();
			}
		});

		daysSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				String itemSelected = (String) arg0.getItemAtPosition(arg2);
				controller.updateDay(itemSelected);
				Toast.makeText(SettingActivity.this, "Days val is Updated!",
						Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

	}

	private void initialize() {
		controller = Controller
				.getInstanceUsingDoubleLocking(SettingActivity.this);
		cityListView = (ListView) this.findViewById(R.id.cityList);
		addBtn = (Button) this.findViewById(R.id.settingAddBtn);
		resetBtn = (Button) this.findViewById(R.id.settingResetBtn);

		showIcons = (CheckBox) this.findViewById(R.id.check_on_of);

		daysSpinner = (Spinner) this.findViewById(R.id.settingDaysSpinner);

		setting = controller.getSettings();

		try {
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, setting.getCities());

			cityListView.setAdapter(adapter);

		} catch (Exception e) {
		}

		try {
			spinnerAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, generateNumbers(6));
			spinnerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			daysSpinner.setAdapter(spinnerAdapter);

			daysSpinner.setSelection(
					Integer.parseInt(setting.getForecastDay()), true);

		} catch (Exception e) {
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			SettingActivity.super.onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		warn();
	}

	private void warn() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Really Exit?");
		builder.setIcon(R.drawable.ic_warning);

		builder.setMessage("Are you sure you want to exit?");

		builder.setNegativeButton("Cancel", null);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SettingActivity.super.onBackPressed();
			}
		});

		dialog = builder.create();

		dialog.getWindow().getAttributes().windowAnimations =

		R.style.DialogAnimation;

		dialog.show();

	}

	private void addCityDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Add New City");
		builder.setIcon(R.drawable.ic_add);

		newCity = new EditText(this);
		newCity.setGravity(Gravity.CENTER);
		builder.setView(newCity);

		builder.setNegativeButton("Cancel", null);

		builder.setPositiveButton("Add City",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						addCity(newCity.getText().toString().trim());
					}
				});

		dialog = builder.create();

		dialog.getWindow().getAttributes().windowAnimations =

		R.style.DialogAnimation;

		dialog.show();
	}

	private void resetDefaultDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Reset To Default?");
		builder.setIcon(R.drawable.ic_default);

		builder.setMessage("Are you sure you want to Reset to the Default Values??");

		builder.setNegativeButton("Cancel", null);

		builder.setPositiveButton("Reset",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						resetValues();
					}
				});

		dialog = builder.create();

		dialog.getWindow().getAttributes().windowAnimations =

		R.style.DialogAnimation;

		dialog.show();
	}

	private void resetValues() {
		controller.reset();
		adapter.clear();
		try {
			adapter = new ArrayAdapter<String>(SettingActivity.this,
					android.R.layout.simple_list_item_1, controller
							.getSettings().getCities());

			cityListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		} catch (Exception e) {
		}
		if (controller.getSettings().getShowIcon().equals("true")) {
			showIcons.setChecked(true);
		} else {
			showIcons.setChecked(false);
		}
		try {
			spinnerAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, generateNumbers(6));
			spinnerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			daysSpinner.setAdapter(spinnerAdapter);

			daysSpinner.setSelection(
					Integer.parseInt(setting.getForecastDay()), true);

		} catch (Exception e) {
		}
	}

	private void addCity(String city) {
		controller.addCity(city);
		adapter.clear();
		try {
			adapter = new ArrayAdapter<String>(SettingActivity.this,
					android.R.layout.simple_list_item_1, controller
							.getSettings().getCities());

			cityListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		} catch (Exception e) {
		}
	}

	private void deleteCityDialog(final String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Delete " + message + "???");
		builder.setIcon(R.drawable.ic_warning);

		builder.setMessage("Are you sure you want to permanently delete "
				+ message + " city?");

		builder.setNegativeButton("Cancel", null);

		builder.setPositiveButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						deleteCity(message);
					}
				});

		dialog = builder.create();

		dialog.getWindow().getAttributes().windowAnimations =

		R.style.DialogAnimation;

		dialog.show();

	}

	private void deleteCity(String city) {
		controller.removeCity(city);
		adapter.clear();
		try {
			adapter = new ArrayAdapter<String>(SettingActivity.this,
					android.R.layout.simple_list_item_1, controller
							.getSettings().getCities());

			cityListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		} catch (Exception e) {
		}

	}

	private String[] generateNumbers(int numLen) {

		String numbers[] = new String[numLen];

		for (int i = 0; i < numLen; i++) {
			numbers[i] = Integer.toString(i);
		}
		return numbers;
	}

}
