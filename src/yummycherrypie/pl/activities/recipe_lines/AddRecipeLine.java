package yummycherrypie.pl.activities.recipe_lines;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.Component;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.dal.repositories.RecipeLineRepository;
import yummycherrypie.pl.Colors;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.WeightExtension;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.adapters.SpinnerAdapter;
import yummycherrypie.system.R;

public class AddRecipeLine extends BaseFragmentActivity implements OnClickListener {
	
	private final String TITLE = "Добавить ингредиент";
	
	private LinearLayout btnAddRecipeLine;
	private Spinner spComponents;
	private EditText etWeight;
	private TextView etComponentName;

	private DBHelper dbHelper;
	private RecipeLineRepository dbr;
	private ComponentRepository compRepo;

	private Map<String, Long> componentsMap;

	private long recipeLineId = -1;
	private long recipeId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_recipe_line);

		SpannableString s = new SpannableString(TITLE);
		s.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		setTitle(s);

		etComponentName = (TextView) findViewById(R.id.tvrecipeComponentName);
		etWeight = (EditText) findViewById(R.id.editTextWeightForRecipeLine);
		btnAddRecipeLine = (LinearLayout) findViewById(R.id.buttonAddRecipeLineForRecipe);
		spComponents = (Spinner) findViewById(R.id.spinnerComponentsForRecipeLine);

		btnAddRecipeLine.setOnClickListener(this);

		dbHelper = new DBHelper(this);
		dbr = new RecipeLineRepository(dbHelper);
		compRepo = new ComponentRepository(dbHelper);

		if (getIntent().getData() != null) {
			String scheme = getIntent().getData().getScheme();
			if (scheme.equals("recipeLineId")) {
				recipeLineId = Long.valueOf(getIntent().getData().getSchemeSpecificPart());
			}
			if (scheme.equals("recipeId")) {
				recipeId = Long.valueOf(getIntent().getData().getSchemeSpecificPart());
			}
		}

		if (recipeLineId == -1)
		{
			RecipeLine newRL = new RecipeLine();
			recipeLineId = dbr.insert(DBHelper.TABLE_RECIPE_LINES, newRL.getInsertedColumns());
			loadComponents(compRepo.getComponentName(1));
		}
		else
		{
			RecipeLine rl = dbr.getRecipeLine(recipeLineId);
			etWeight.setText(WeightExtension.getWeight(rl.getWeight()));
			loadComponents(compRepo.getComponentName(rl.getComponentId()));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonAddRecipeLineForRecipe:
			try {
				long compId = componentsMap.get(spComponents.getSelectedItem().toString());
				Component component = compRepo.getComponent(compId);
				RecipeLine newRL = new RecipeLine(
						component.isCountable() ? 0 : nf.parse(etWeight.getText().toString()).doubleValue(),
						component.isCountable() ? Integer.valueOf(etWeight.getText().toString()) : 0,
						compId,
						recipeId,
						Booking.DEFAULT_BOOKING_ID,
						RecipeLine.State.ADD_BY_USER);
				dbr.update(DBHelper.TABLE_RECIPE_LINES, newRL.getUpdatedColumns(), recipeLineId);
				Intent intent = new Intent();
				intent.putExtra("componentName", spComponents.getSelectedItem().toString());
				setResult(RESULT_OK, intent);
				finish();
			}
			catch(Exception e)
			{
				Toast.makeText(this, "При создании строки рецепта возникла ошибка", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		}
	}

	private void loadComponents(String componentName) {
		ArrayList<Component> components = compRepo.getAllComponents();
		ArrayList<String> sComponents = new ArrayList<String>();
		componentsMap = new HashMap<String, Long>();
		for (Component c : components) {
			String name = c.getName();
			sComponents.add(name == null ? "Новый ингредиент" : name);
			componentsMap.put(c.getName(), c.getId());
		}
		SpinnerAdapter adapter = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, sComponents);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spComponents.setAdapter(adapter);

		Spannable wordtoSpan = new SpannableString("Ингредиент");
		wordtoSpan.setSpan(new ForegroundColorSpan(Colors.COLOR_DEFAULT_TEXT), 0,
				wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spComponents.setPrompt(wordtoSpan);
		spComponents.setSelection(adapter.getPosition(componentName));
	}

	@Override
	protected void onDestroy() {
	  super.onDestroy();
	  if (dbr != null)
	  	dbr.close();
	}
}
