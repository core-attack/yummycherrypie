package yummycherrypie.pl.activities.recipes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.Recipe;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.dal.repositories.RecipeLineRepository;
import yummycherrypie.dal.repositories.RecipeRepository;
import yummycherrypie.pl.GridViewExtension;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.WeightExtension;
import yummycherrypie.pl.activities.BaseDialogActivity;
import yummycherrypie.pl.activities.recipe_lines.AddRecipeLine;
import yummycherrypie.pl.activities.recipe_lines.AddRecipeLineDialog;
import yummycherrypie.pl.adapters.RecipeComponentAdapter;
import yummycherrypie.system.R;

public class AddRecipe extends BaseDialogActivity implements OnClickListener, AddRecipeLineDialog.IDialogListener {

	private final int MENU_UPDATE = 1;
	private final int MENU_DELETE = 2;
	private final String ADD_RECIPE = "Добавить рецепт";
	private final String VIEW_RECIPE = "Просмотр рецепта";

	private LinearLayout btnSaveRecipe;
	private LinearLayout btnAddRecipeLine;
	private GridView gridViewComponents;
	private EditText etName;
	private EditText etCakeWeight;
	private EditText etCountProduct;
	private CheckBox cbCountable;
	
	private DBHelper dbHelper;
	private RecipeRepository dbr;
	private RecipeLineRepository rlRepo;
	private ComponentRepository compRepo;

	private RecipeComponentAdapter rcAdapter;

	private long recipeId = -1;
	//если первый раз входим в редактирование записи, то обрабатываем создание, иначе - обновление
	private boolean isFirstEntry = false;
	//если зашли сюда с AddBooking
	private boolean isRequestFromAddBooking = false;

	private Map<Integer, Long> recipeLinesIds;

	private int oldValueCountProduct;
	private double oldValueWeight;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_recipe);

		btnSaveRecipe = (LinearLayout) findViewById(R.id.buttonSaveRecipe);
		btnAddRecipeLine = (LinearLayout) findViewById(R.id.btnAddRecipeLine);
		etName = (EditText) findViewById(R.id.editTextBookingName);
		etCakeWeight = (EditText) findViewById(R.id.editTextCakeWeight);
		etCountProduct = (EditText) findViewById(R.id.editTextCountProduct);
		cbCountable = (CheckBox) findViewById(R.id.checkBoxCountable);

		cbCountable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				viewEditTexts(isChecked);
			}
		});

		etCakeWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					try {
						oldValueWeight = nf.parse(etCakeWeight.getText().toString()).doubleValue();
					} catch (Exception e) {
						oldValueWeight = 0;
					}
					etCakeWeight.setText("");
				} else {
					if (etCakeWeight.getText().toString().isEmpty()) {
						etCakeWeight.setText(WeightExtension.getWeight(oldValueWeight));
					}
				}
			}
		});

		etCountProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					try {
						oldValueCountProduct = nf.parse(etCountProduct.getText().toString()).intValue();
					}
					catch (Exception e){
						oldValueCountProduct = 0;
					}
					etCountProduct.setText("");
				} else {
					if (etCountProduct.getText().toString().isEmpty()){
						etCountProduct.setText(String.valueOf(oldValueCountProduct));
					}
				}
			}
		});

		btnSaveRecipe.setOnClickListener(this);
		btnAddRecipeLine.setOnClickListener(this);

		dbHelper = new DBHelper(this);
		dbr = new RecipeRepository(dbHelper);
		rlRepo = new RecipeLineRepository(dbHelper);
		compRepo = new ComponentRepository(dbHelper);

		if (getIntent().getData().getScheme().contains("createNewRecipeFromBooking")){
			isRequestFromAddBooking = true;
			setTitle(ADD_RECIPE);
		}
		else {
			recipeId = Long.valueOf(getIntent().getData().getSchemeSpecificPart());
			setTitle(VIEW_RECIPE);
		}

		SpannableString s = new SpannableString(getTitle());
		s.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		setTitle(s);

		if (recipeId == -1) {
			isFirstEntry = true;
			Recipe newRecipe = new Recipe();
			newRecipe.setCreateDate(new Date().getTime());
			recipeId = dbr.insert(DBHelper.TABLE_RECIPES, newRecipe.getInsertedColumns());
			viewEditTexts(false);
		}
		else
		{
			isFirstEntry = false;
			Recipe r = dbr.getRecipe(recipeId);
			etName.setText(r.getName());
			etCakeWeight.setText(WeightExtension.getWeight(r.getCakeWeight()));
			etCountProduct.setText(String.valueOf(r.getCountProduct()));
			updateListViewRecipeLines();
			viewEditTexts(r.isCountable());
			cbCountable.setChecked(r.isCountable());
		}

	}

	private void viewEditTexts(boolean isCountable){
		if (isCountable) {
			etCakeWeight.setVisibility(View.INVISIBLE);
			etCountProduct.setVisibility(View.VISIBLE);
		} else {
			etCakeWeight.setVisibility(View.VISIBLE);
			etCountProduct.setVisibility(View.INVISIBLE);
		}
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.buttonSaveRecipe:
			try {
				Date d = new Date();
				double weight = 0;
				int count = 0;
				try{
					weight = nf.parse(etCakeWeight.getText().toString()).doubleValue();
				} catch (ParseException e){
					weight = 0;
				}
				try{
					count = nf.parse(etCountProduct.getText().toString()).intValue();
				} catch (ParseException e){
					count = 0;
				}
				Recipe r = new Recipe(etName.getText().toString(), weight, count, cbCountable.isChecked());
				r.setUpdateDate(d.getTime());
				dbr.update(DBHelper.TABLE_RECIPES, r.getUpdatedColumns(), recipeId);
				intent = new Intent();
				intent.setData(Uri.parse("recipeId:" + recipeId));
				setResult(RESULT_OK, intent);
				finish();
				Toast.makeText(this, "Рецепт сохранен", Toast.LENGTH_SHORT)
						.show();
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При обновлении рецепта возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.btnAddRecipeLine:
			try {
				intent = new Intent(this, AddRecipeLine.class);
				intent.setData(Uri.parse("recipeId:" + recipeId));
				startActivityForResult(intent, 1);
				break;
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При обновлении рецепта возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	void updateListViewRecipeLines() {
		ArrayList<RecipeLine> recipeLines = rlRepo.getRecipeLines(recipeId);

		recipeLinesIds = new HashMap<Integer, Long>();
		for (int i = 0; i < recipeLines.size(); i++) {
			recipeLinesIds.put(i, recipeLines.get(i).getId());
		}

		if(recipeLines.size() % 2 != 0) {
			ArrayList<RecipeLine> evenRecipeLines = new ArrayList<RecipeLine>();
			for(RecipeLine rl : recipeLines) {
				evenRecipeLines.add(rl);
			}
			evenRecipeLines.add(null);
			rcAdapter = new RecipeComponentAdapter(this, R.layout.item_recipe_component, evenRecipeLines, compRepo);
		}
		else {
			rcAdapter = new RecipeComponentAdapter(this, R.layout.item_recipe_component, recipeLines, compRepo);
		}

		gridViewComponents = (GridView) findViewById(R.id.gridViewSelectedComponents);
		gridViewComponents.setAdapter(rcAdapter);
		registerForContextMenu(gridViewComponents);
		GridViewExtension.setGridViewHeightBasedOnChildren(this, gridViewComponents, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		updateListViewRecipeLines();
	}

	/**
	 * создание контестного меню
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenu.ContextMenuInfo menuInfo) {
		switch (v.getId()) {
			case R.id.gridViewSelectedComponents:
				menu.add(0, MENU_UPDATE, 0, "Изменить");
				menu.add(0, MENU_DELETE, 0, "Удалить");
				break;
		}
	}

	/**
	 * обработка опций контекстного меню
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo acmi;
		switch (item.getItemId()) {
			case MENU_UPDATE:
				try {
					acmi = (AdapterView.AdapterContextMenuInfo) item
							.getMenuInfo();
					long id = -1;
					try {
						id = recipeLinesIds.get((int) acmi.id);
					}
					finally {
						showAddOrChooseRecipeLineDialog(id);
						updateListViewRecipeLines();
					}
				}
				catch(Exception e)
				{
					Toast.makeText(this, "При обновлении строки рецепта возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case MENU_DELETE:
				try {
					acmi = (AdapterView.AdapterContextMenuInfo) item
							.getMenuInfo();
					if (recipeLinesIds.get((int) acmi.id) == null){
						Toast.makeText(this, "Нечего удалять", Toast.LENGTH_SHORT)
								.show();
					}
					else {
						RecipeLine rl = rlRepo.getRecipeLine(recipeLinesIds.get((int) acmi.id));
						dbr.delete(DBHelper.TABLE_RECIPE_LINES, rl.getId());
						Toast.makeText(this, "Ингридиент удален из заказа", Toast.LENGTH_SHORT)
								.show();
						updateListViewRecipeLines();
					}
				}
				catch(Exception e)
				{
					Toast.makeText(this, "При удалении строки рецепта возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onDestroy() {
	  super.onDestroy();
	  if (dbr != null)
	  	dbr.close();
	}

	public void showAddOrChooseRecipeLineDialog(long recipeLineId) {
		DialogFragment dialog = AddRecipeLineDialog.newInstance(Booking.DEFAULT_BOOKING_ID, recipeLineId, recipeId);
		dialog.show(getSupportFragmentManager(), "AddRecipeLineDialog");
	}

	@Override
	public void onRecipeLineDialogPositiveClick(DialogFragment dialog, long recipeLineId, long recipeId) {
		updateListViewRecipeLines();
	}

	@Override
	public void onRecipeLineDialogNegativeClick(DialogFragment dialog) {

	}
}
