package yummycherrypie.pl.activities.recipes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import yummycherrypie.base_classes.Recipe;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.RecipeRepository;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.adapters.RecipeAdapter;
import yummycherrypie.system.R;

public class Recipes extends BaseFragmentActivity implements OnClickListener,
		OnItemClickListener, LoaderCallbacks<Cursor>{
	final String LOG_TAG = "myLogs";
	final int MENU_UPDATE = 1;
	final int MENU_DELETE = 2;

	DBHelper dbHelper;
	RecipeRepository dbr;
	
	RecipeAdapter rAdapter;
	Cursor cursor;

	ListView listRecipes;
	LinearLayout btnAddRecipe;

	Map<Integer, Long> recipesMap = new HashMap<Integer, Long>();

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipes);

		btnAddRecipe = (LinearLayout) findViewById(R.id.buttonAddRecipe);
		btnAddRecipe.setOnClickListener(this);

		dbHelper = new DBHelper(this);
		dbr = new RecipeRepository(dbHelper);

		loadRecipes();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		switch (v.getId()) {
			case R.id.listViewExistingRecipes:
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
				Intent intent = new Intent(this, AddRecipe.class);
				AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
						.getMenuInfo();
				intent.setData(Uri.parse("recipeId:" + acmi.id));
				startActivityForResult(intent, 1);
				getSupportLoaderManager().getLoader(0).forceLoad();
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При обновлении рецепта возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case MENU_DELETE:
			try {
				final AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
				Recipe r = dbr.getRecipe(acmi.id);

				new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Подтвердите удаление")
					.setMessage("Удалить запись '" + r.getName() + "'?")
						.setPositiveButton("Да", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dbr.delete(DBHelper.TABLE_RECIPES, acmi.id);
								getSupportLoaderManager().getLoader(0).forceLoad();
								Toast.makeText(getApplicationContext(), "Рецепт удален", Toast.LENGTH_SHORT)
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
				Toast.makeText(this, "При удалении рецепта возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
		return super.onContextItemSelected(item);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.buttonAddRecipe:
			try {
				intent = new Intent(this, AddRecipe.class);
				intent.setData(Uri.parse("recipeId:" + Long.valueOf(-1)));
				startActivityForResult(intent, 1);
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При создании рецепта возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
	}

	// получаем значение типа торта из другого activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		getSupportLoaderManager().getLoader(0).forceLoad();
		Toast.makeText(this, "Рецепт сохранен", Toast.LENGTH_SHORT)
		.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		try {
			Intent intent = new Intent(this, AddRecipe.class);
			intent.setData(Uri.parse("recipeId:" + id));
			startActivityForResult(intent, 1);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "При создании рецепта возникла ошибка", Toast.LENGTH_SHORT)
					.show();
		}
	}

	void loadRecipes() {
		rAdapter = new RecipeAdapter(this, null);
		listRecipes = (ListView) findViewById(R.id.listViewExistingRecipes);
		listRecipes.setAdapter(rAdapter);
		listRecipes.setOnItemClickListener(this);
		registerForContextMenu(listRecipes);
		getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
	  return new MyCursorLoader(this, dbr);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	  rAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
	  
	static class MyCursorLoader extends CursorLoader {

		RecipeRepository db;

		public MyCursorLoader(Context context, RecipeRepository db) {
			super(context);
			this.db = db;
		}

		@Override
		public Cursor loadInBackground() {
			Cursor cursor = db.getAllRecords(DBHelper.TABLE_RECIPES, DBHelper.COLUMN_RECIPES_NAME);
			return cursor;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbr != null)
			dbr.close();
	}
}
