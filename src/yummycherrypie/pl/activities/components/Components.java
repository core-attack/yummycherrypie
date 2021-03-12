package yummycherrypie.pl.activities.components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import yummycherrypie.base_classes.Component;
import yummycherrypie.business_logic.Extensions.UpdateCurrentBooking;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.dal.repositories.RecipeLineRepository;
import yummycherrypie.dal.repositories.RecipeRepository;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.adapters.ComponentAdapter;
import yummycherrypie.system.R;

public class Components extends BaseFragmentActivity implements OnClickListener,
OnItemClickListener, LoaderCallbacks<Cursor> {

	private final int MENU_UPDATE = 1;
	private final int MENU_DELETE = 2;

	private DBHelper dbHelper;
	private SQLiteDatabase db;
	private ComponentRepository componentRepository;

	private ComponentAdapter cAdapter;

	private ListView listComponents;
	private LinearLayout btnAddComponent;
	private Button btnRecalc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.components);

		btnAddComponent = (LinearLayout) findViewById(R.id.buttonSaveComponent);
		btnRecalc = (Button) findViewById(R.id.buttonRecalc);
		btnAddComponent.setOnClickListener(this);
		btnRecalc.setOnClickListener(this);

		dbHelper = new DBHelper(this);
		componentRepository = new ComponentRepository(dbHelper);

		loadComponents();
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
			case R.id.buttonSaveComponent:
				try {
					intent = new Intent(this, AddComponent.class);
					intent.setData(Uri.parse("componentId:" + (-1)));
					startActivityForResult(intent, 1);
					break;
				}
				catch(Exception e)
				{
					Toast.makeText(this, "При создании ингридиента возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
			case R.id.buttonRecalc:
			try {
				UpdateCurrentBooking.doIt(this, new BookingRepository(dbHelper), new RecipeLineRepository(dbHelper), new RecipeRepository(dbHelper), componentRepository);
				break;
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При создании ингридиента возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	// получаем значение типа торта из другого activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		getSupportLoaderManager().getLoader(0).forceLoad();
		Toast.makeText(this, "Ингридиент сохранен", Toast.LENGTH_SHORT)
		.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try{
			Intent intent = new Intent(this, AddComponent.class);
			intent.setData(Uri.parse("componentId:" + id));
			startActivityForResult(intent, 1);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "При создании ингридиента возникла ошибка", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		switch (v.getId()) {
		case R.id.listViewComponents:
			menu.add(0, MENU_UPDATE, 0, "Изменить");
			menu.add(0, MENU_DELETE, 0, "Удалить");
			break;
		}

		MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				onContextItemSelected(item);
				return true;
			}
		};

		for (int i = 0, n = menu.size(); i < n; i++)
			menu.getItem(i).setOnMenuItemClickListener(listener);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_UPDATE:
			try {
				Intent intent = new Intent(this, AddComponent.class);
				AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
						.getMenuInfo();
				intent.setData(Uri.parse("componentId:" + acmi.id));
				startActivityForResult(intent, 1);
				getSupportLoaderManager().getLoader(0).forceLoad();
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При обновлении ингридиента возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case MENU_DELETE:
			try {
				final AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
				Component c = componentRepository.getComponent(acmi.id);

				new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Подтвердите удаление")
					.setMessage("Удалить запись '" + c.getName() + "'?")
						.setPositiveButton("Да", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								componentRepository.delete(DBHelper.TABLE_COMPONENTS, acmi.id);
								getSupportLoaderManager().getLoader(0).forceLoad();
								Toast.makeText(getApplicationContext(), "Ингридиент удален", Toast.LENGTH_SHORT)
										.show();
							}
						}).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					}).show();
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При удалении ингридиента возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void loadComponents() {
		cAdapter = new ComponentAdapter(this, null);
		listComponents = (ListView) findViewById(R.id.listViewComponents);
		listComponents.setAdapter(cAdapter);
		listComponents.setOnItemClickListener(this);
		registerForContextMenu(listComponents);
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
		return new MyCursorLoader(this, componentRepository);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	    cAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}

	private static class MyCursorLoader extends CursorLoader {

		ComponentRepository db;

		public MyCursorLoader(Context context, ComponentRepository db) {
			super(context);
			this.db = db;
		}

		@Override
		public Cursor loadInBackground() {
			Cursor cursor = db.getAllRecords(DBHelper.TABLE_COMPONENTS, DBHelper.COLUMN_COMPONENTS_NAME);
			return cursor;
		}
	}

	@Override
	protected void onDestroy() {
	  super.onDestroy();
	  if (componentRepository != null)
	  	componentRepository.close();
	}
}
