package yummycherrypie.pl.activities.bookings;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.BookingMan;
import yummycherrypie.base_classes.BookingType;
import yummycherrypie.base_classes.Event;
import yummycherrypie.base_classes.Recipe;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.business_logic.Calculation;
import yummycherrypie.business_logic.Extensions.OtherExtensions;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.BookingManRepository;
import yummycherrypie.dal.repositories.BookingRepository;
import yummycherrypie.dal.repositories.BookingTypeRepository;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.dal.repositories.EventRepository;
import yummycherrypie.dal.repositories.RecipeLineRepository;
import yummycherrypie.dal.repositories.RecipeRepository;
import yummycherrypie.system.R;
import yummycherrypie.pl.CountExtension;
import yummycherrypie.pl.CurrencyExtension;
import yummycherrypie.pl.DateExtension;
import yummycherrypie.pl.GridViewExtension;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.WeightExtension;
import yummycherrypie.pl.activities.BaseFragmentActivity;
import yummycherrypie.pl.activities.booking_men.AddBookingManDialog;
import yummycherrypie.pl.activities.events.AddOrChooseEvent;
import yummycherrypie.pl.activities.recipe_lines.AddRecipeLineDialog;
import yummycherrypie.pl.activities.recipes.AddRecipe;
import yummycherrypie.pl.adapters.BookingComponentAdapter;
import yummycherrypie.pl.adapters.BookingTypeGridViewAdapter;
import yummycherrypie.pl.adapters.SpinnerAdapter;
import yummycherrypie.pl.adapters.SpinnerSubtextAdapter;

public class AddBooking extends BaseFragmentActivity implements OnClickListener,
		AddRecipeLineDialog.IDialogListener,
		AddBookingManDialog.IDialogListener	{

	private final int MENU_UPDATE = 1;
	private final int MENU_DELETE = 2;
	private final int MAX_SPINNER_EVENTS_ITEM_LENGTH = 35;
	private final int DEFAULT_COUNT_PRODUCT = 1;
	private final int DEFAULT_TIME_SPENT = 0;
	private final String DEFAULT_SPINNER_RECORD = "Не выбран";
	private final String IN_ROUBLE = "i";
	private final String PER_COUNT = "За штуку";

	/**
	 * перечисление для загрузки строк рецепта
	 */
	public static enum RecipeLineLoading{
		LOAD_TEMPLATE_COMPONENTS,
		LOAD_ALL_COMPONENTS
	};

	private DBHelper dbHelper;
	private BookingRepository bookingRepo;
	private RecipeRepository recipeRepo;
	private BookingManRepository bmRepo;
	private BookingTypeRepository btRepo;
	private ComponentRepository compRepo;
	private EventRepository eventRepo;
	private RecipeLineRepository rlRepo;

    private TextView tvTime;
    private TextView tvDate;
    private TextView tvProceeds;
	private TextView tvEvent;
	private TextView tvRecipePrice;
	private TextView tvResultPrice;
	private TextView tvTimeSpent;

	private EditText etComment;
    private EditText etCakePrice;
    private EditText etWeight;
	private EditText etDiscount;
	private EditText etCountProduct;

	private SeekBar sbTimeSpent;

    private Spinner spRecipe;
    private Spinner spBookingMan;

	private LinearLayout btnSaveBooking;
	private Button btnAddBookingMan;
	private Button btnAddComponent;
	private Button btnAddEvent;
	private Button btnAddRecipe;

	private GridView gvBookingTypes;

	private GridView gridViewAddedComponents;

	private ArrayList<RecipeLine> recipeLines = new ArrayList<RecipeLine>();

	private int DIALOG_DATE = 1;
	private int DIALOG_TIME = 2;

	private long eventId = -1;
	private long recipeId = -1;
	private long bookingId = -1;
	private long bookingManId = -1;
	private long selectedDate = -1;
	private long bookingTypeId = -1;

	protected Recipe currentRecipe = null;

	private Map<String, Long> recipeMap;
	private Map<Integer, Long> recipeIds;
	private Map<String, Long> bMenMap;
	private Map<Integer, Long> bMenIds;
	private Map<Integer, Long> recipeLinesIds;

	private BookingComponentAdapter bcAdapter;

	private double oldValueCakePrice;
	private double oldValueWeight;
	private double oldValueDiscount;
	private double oldValueTimeSpent;
	private double oldValueCountProduct;

	//только что зашли в активити?
	private boolean firstEntry = true;
	//состояние создания заказа или его редактирование
	private boolean isCreateState  = false;

	private double price = 0;
	private double recipePrice = 0;
	private double discount = 0;
	private int countProduct = 0;
	private double weight = 0;

	/**
	 * действия при создании активити
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_booking);
		
		dbHelper = new DBHelper(this);
		bookingRepo = new BookingRepository(dbHelper);
		recipeRepo = new RecipeRepository(dbHelper);
		bmRepo = new BookingManRepository(dbHelper);
		btRepo = new BookingTypeRepository(dbHelper);
		compRepo = new ComponentRepository(dbHelper);
		eventRepo = new EventRepository(dbHelper);
		rlRepo = new RecipeLineRepository(dbHelper);

		setVariables();

		if (getIntent().getData() != null) {
			String data = getIntent().getData().getSchemeSpecificPart();
			String[] values = data.split(",");
			if (getIntent().getData().getScheme().equals("eventId")){
				eventId = values.length > 0 ? Long.valueOf(values[0]) : -1;
				Event ev = eventRepo.getEvent(eventId);
				selectedDate = ev.getDateLong();
				bookingManId = ev.getBookingManId();
			}
			else{
				selectedDate = values.length > 0 ? Long.valueOf(values[0]) : -1;
				bookingId = values.length > 1 ? Long.valueOf(values[1]) : -1;
			}
		}
        if (bookingId == -1){
			isCreateState = true;

			Booking newBooking = new Booking();

			newBooking.setCreateDate(new Date().getTime());
			newBooking.setDateLong(selectedDate);

			bookingId = bookingRepo.insert(DBHelper.TABLE_BOOKINGS, newBooking.getInsertedColumns());
			if (selectedDate != -1) {
				tvDate.setText(DateExtension.getDate(selectedDate));
				tvTime.setText(DateExtension.getTime(selectedDate));
			} else {
				tvDate.setText(DateExtension.getDate(Calendar.getInstance().getTime().getTime()));
				tvTime.setText(DateExtension.getTime(Calendar.getInstance().getTime().getTime()));
			}

			updateBookingTypes();

			updateSpinnerRecipes(null);

			updateSpinnerBookingMan(bmRepo.getBookingMan(bookingManId));

			updateGridViewRecipeLines(RecipeLineLoading.LOAD_TEMPLATE_COMPONENTS);

			SetEvent(-1);

			etCountProduct.setText(String.valueOf(DEFAULT_COUNT_PRODUCT));
			sbTimeSpent.setProgress(0);
			tvTimeSpent.setText(String.format("%d ч.", 0));

			UpdateEditText(getResultPrice(price, countProduct), discount, recipePrice, countProduct);

			etCakePrice.setTypeface(Typeface.createFromAsset(getAssets(), StringExtension.ROUBLE_FONT));

			boolean isCountable = false;
			chooseWeightOrCount(isCountable);
        }
        else {
			isCreateState = false;

        	Booking b = bookingRepo.getBooking(bookingId);

			bookingTypeId = b.getBookingTypeId();
			updateBookingTypes();

			updateSpinnerRecipes(recipeRepo.getRecipe(b.getRecipeId()));

			updateSpinnerBookingMan(bmRepo.getBookingMan(b.getBookingManId()));

			updateGridViewRecipeLines(RecipeLineLoading.LOAD_ALL_COMPONENTS);

			SetEvent(b.getEventId());

			sbTimeSpent.setProgress(b.getTimeSpent());
			tvTimeSpent.setText(String.format("%d ч.", b.getTimeSpent()));
			etComment.setText(b.getComment());
			etDiscount.setText(String.format(OtherExtensions.DEFAULT_LOCALE, "%,d", Double.valueOf(b.getDiscount()).intValue()));
			etCountProduct.setText(String.valueOf(b.getCountProduct()));
			etWeight.setText(WeightExtension.getWeight(b.getWeight()));
			tvDate.setText(DateExtension.getDate(b.getDateLong()));
			tvTime.setText(DateExtension.getTime(b.getDateLong()));
			recipePrice = b.getRecipePrice();
			tvRecipePrice.setText(CurrencyExtension.getCurrencyWithoutRouble(b.getRecipePrice()));

			double resultPrice = getResultPrice(b.getCakePrice(), b.getCountProduct());
			etCakePrice.setText(CurrencyExtension.getCurrencyDecimalWithoutRouble(resultPrice));
			UpdateEditText(resultPrice, b.getDiscount(), b.getRecipePrice(), b.getCountProduct());

        }

		setListeners();
	}

	/**
	 * заполняем переменные
	 */
	private void setVariables(){

		tvTime = (TextView) findViewById(R.id.tvBookingTime);
		tvDate = (TextView) findViewById(R.id.tvBookingDate);
		tvEvent = (TextView) findViewById(R.id.textViewEvent);
		tvRecipePrice = (TextView) findViewById(R.id.tvRecipePrice);
		tvProceeds = (TextView) findViewById(R.id.tvProceeds);
		tvResultPrice = (TextView) findViewById(R.id.tvResultPrice);
		tvTimeSpent = (TextView) findViewById(R.id.tvTimeSpent);

		etComment = (EditText) findViewById(R.id.editTextComment);
		etCakePrice = (EditText) findViewById(R.id.editTextCakePrice);
		etWeight = (EditText) findViewById(R.id.editTextBookingWeight);
		etDiscount = (EditText) findViewById(R.id.editTextCakeSale);
		etCountProduct = (EditText) findViewById(R.id.etCountProduct);

		sbTimeSpent = (SeekBar) findViewById(R.id.seekBarTimeSpent);
		// you should define max in xml, but if you need to do this by code,
		// you must set max as 0 and then your desired value. this is because a bug
		// in SeekBar (issue 12945) (don't really checked if it was corrected)
		sbTimeSpent.setMax(0);
		sbTimeSpent.setMax(24);


		//listAddedComponents = (ListView) findViewById(R.id.listViewAddedComponents);
		//listAddedComponents.setOnItemClickListener(this);

		btnAddEvent = (Button) findViewById(R.id.btnAddEvent);
		btnAddBookingMan = (Button) findViewById(R.id.btnAddBookingMan);
		btnAddComponent = (Button) findViewById(R.id.buttonAddComponent);
		btnSaveBooking = (LinearLayout) findViewById(R.id.buttonSaveBooking);
		btnAddRecipe = (Button) findViewById(R.id.btnAddRecipe);

		btnSaveBooking.setOnClickListener(this);
		btnAddBookingMan.setOnClickListener(this);
		btnAddComponent.setOnClickListener(this);
		btnAddEvent.setOnClickListener(this);
		btnAddRecipe.setOnClickListener(this);

		gvBookingTypes = (GridView) findViewById(R.id.gvBookingTypes);

		gridViewAddedComponents = (GridView) findViewById(R.id.gridViewAddedComponents);
	}

	/**
	 * устанавливаем обработчики
	 */
	private void setListeners(){

		etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					OnWeightChanged();
					if (etWeight.getText().toString().isEmpty()) {
						etWeight.setText(WeightExtension.getWeight(oldValueWeight));
					}
				} else {
					try {
						oldValueWeight = nf.parse(etWeight.getText().toString()).doubleValue();
					} catch (Exception e) {
						oldValueWeight = 0;
					}
					etWeight.setText("");
				}
			}
		});

		etWeight.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:
							OnWeightChanged();
							break;
					}
				}
				return false;
			}
		});

		//обработчик изменения текста стоимости
		etCakePrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					if (etCakePrice.getText().toString().length() > 0) {
						try {
							price = nf.parse(etCakePrice.getText().toString()).doubleValue();
						}
						catch (Exception e){
							Toast.makeText(getApplicationContext(), "Не удалось считать стоимость", Toast.LENGTH_SHORT).show();
						}
						UpdateEditText(price, discount, recipePrice, countProduct);
						etCakePrice.setTypeface(Typeface.createFromAsset(getAssets(), StringExtension.DEFAULT_FONT));
					}
					else{
						etCakePrice.setTypeface(Typeface.createFromAsset(getAssets(), StringExtension.ROUBLE_FONT));
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Что то пошло не так в etCakePrice.addTextChangedListener", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		//обработчик изменения текста скидки
		etDiscount.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				try {
					if (s.toString().length() > 0) {
						try {
							discount = nf.parse(s.toString()).doubleValue();
						} catch (Exception e){
							Toast.makeText(getApplicationContext(), "Не удалось считать скидку", Toast.LENGTH_SHORT).show();
						}
						if (discount <= 100) {
							UpdateEditText(price, discount, recipePrice, countProduct);
						} else {
							Toast.makeText(getApplicationContext(), "Сумма скидки не может быть больше 100%!", Toast.LENGTH_SHORT).show();
						}
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Что то пошло не так в etDiscount.addTextChangedListener", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		//если нужно будет включить прокрутку внутреннего списка в scrollview, раскомментировать код ниже
		//по умолчанию родительский scrollview блокирует скроллинг в listview
		//обходим это ограничение
		/*
		listAddedComponents.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						v.getParent().requestDisallowInterceptTouchEvent(true);
						break;
					case MotionEvent.ACTION_UP:
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
				}
				v.onTouchEvent(event);
				return true;
			}
		});
		*/

		etCakePrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {
					etCakePrice.setTypeface(Typeface.createFromAsset(getAssets(), StringExtension.ROUBLE_FONT));
					try {
						oldValueCakePrice = nf.parse(etCakePrice.getText().toString()).doubleValue();
					} catch (Exception e) {
						oldValueCakePrice = 0;
					}
					etCakePrice.setText("");
				} else {
					if (etCakePrice.getText().toString().isEmpty()) {
						etCakePrice.setText(CurrencyExtension.getCurrencyDecimalWithoutRouble(oldValueCakePrice));
					}
					etCakePrice.setTypeface(Typeface.createFromAsset(getAssets(), StringExtension.DEFAULT_FONT));
				}
			}
		});

		etDiscount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					try {
						oldValueDiscount = nf.parse(etDiscount.getText().toString()).doubleValue();
					} catch (Exception e) {
						oldValueDiscount = 0;
					}
					etDiscount.setText("");
				} else {
					if (etDiscount.getText().toString().isEmpty()) {
						etDiscount.setText(String.format(OtherExtensions.DEFAULT_LOCALE, "%,d", Double.valueOf(oldValueDiscount).intValue()));
					}
				}
			}
		});

		etCountProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					try {
						oldValueCountProduct = nf.parse(etCountProduct.getText().toString()).doubleValue();
					} catch (Exception e) {
						oldValueCountProduct = 0;
					}
					etCountProduct.setText("");
				} else {
					OnCountProductChanged();
					if (etCountProduct.getText().toString().isEmpty()) {
						etCountProduct.setText(String.format(OtherExtensions.DEFAULT_LOCALE, "%,d", Double.valueOf(oldValueCountProduct).intValue()));
					}
				}
			}
		});


		etCountProduct.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
						case KeyEvent.KEYCODE_ENTER:
							OnCountProductChanged();
							break;
					}
				}
				return false;
			}
		});

		sbTimeSpent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvTimeSpent.setText(String.format("%d ч.", progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
	}

	/**
	 * действие при изменении веса заказа
	 */
	private void OnWeightChanged(){
		try{

			if (etWeight.getText().toString().length() > 0)
			{
				try {
					weight = nf.parse(etWeight.getText().toString()).doubleValue();
				}
				catch (Exception e){
					Toast.makeText(getApplicationContext(), "Не удалось считать вес", Toast.LENGTH_SHORT).show();
				}
				if (weight > 0){
					calculateSummary();
				}
			}
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "Что то пошло не так в etWeight.setOnFocusChangeListener", Toast.LENGTH_SHORT).show();
		}
	}

	private void calculateSummary(){
		if (recipeId <= 0){
			Toast.makeText(getApplicationContext(), "Выберите рецепт!", Toast.LENGTH_SHORT).show();
		} else {
			Calculation calc = new Calculation(recipeRepo, rlRepo, compRepo, recipeId, bookingId, getApplicationContext(), recipeLines);
			if (currentRecipe.isCountable()) {
				calc.setSummary(countProduct);
			} else {
				calc.setSummary(weight);
			}
			calc.updateRecipeLinesArray();
			updateGridViewRecipeLines(RecipeLineLoading.LOAD_ALL_COMPONENTS);
			recipePrice = calc.getSummary();
			tvRecipePrice.setText(String.format(Locale.ROOT, "%.0f", recipePrice), TextView.BufferType.EDITABLE);
			UpdateEditText(price, discount, recipePrice, countProduct);
		}
	}

	/**
	 * действие при изменении количества заказа
	 */
	private void OnCountProductChanged(){
		try{

			if (etCountProduct.getText().toString().length() > 0)
			{
				try{
					countProduct = nf.parse(etCountProduct.getText().toString()).intValue();
				}
				catch (Exception e){
					Toast.makeText(getApplicationContext(), "Не удалось считать вес", Toast.LENGTH_SHORT).show();
				}
				if (countProduct > 0){
					calculateSummary();
				}
			}
		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), "Что то пошло не так в etWeight.setOnFocusChangeListener", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * обновление контрола
	 */
	private void UpdateEditText(double price, double discount, double recipePrice, int count){
		tvProceeds.setText(CurrencyExtension.getCurrencyDecimalWithoutRouble(Calculation.getFinallyPrice(getResultPrice(price, count), discount, recipePrice)));
		tvResultPrice.setText(CurrencyExtension.getCurrencyDecimalWithoutRouble(Calculation.getDiscountPrice(getResultPrice(price, count), discount)));
	}

	private double getResultPrice(double price, double count){
		if (currentRecipe == null){
			return price;
		}
		return Calculation.getResultPrice(price, count, currentRecipe.isCountable());
	}

	private String getItemRecipeText(Recipe r){
		return String.format("%s (%s)", r.getName(), r.isCountable() ? CountExtension.COUNT : WeightExtension.GRAMM);
	}

	/**
	 * выпадающий список рецептов
	 */
	private void updateSpinnerRecipes(Recipe recipe) {
		try {
			this.currentRecipe = recipe;
			ArrayList<Recipe> recipes = recipeRepo.getAllRecipes();
			ArrayList<String> sRecipes = new ArrayList<String>();
			recipeMap = new HashMap<String, Long>();
			recipeIds = new HashMap<Integer, Long>();
			int i = 0;

			sRecipes.add(DEFAULT_SPINNER_RECORD);
			recipeMap.put(DEFAULT_SPINNER_RECORD, Long.valueOf(-1));
			recipeIds.put(i, Long.valueOf(-1));
			i++;

			for (Recipe r : recipes) {
				String s = getItemRecipeText(r);
				sRecipes.add(s);
				recipeMap.put(s, r.getId());
				recipeIds.put(i, r.getId());
				i++;
			}
			// адаптер
			SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, sRecipes);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spRecipe = (Spinner) findViewById(R.id.spinnerChooseRecipe);
			spRecipe.setAdapter(adapter);
			spRecipe.setPrompt("Рецепт");

			if (recipe != null)
				spRecipe.setSelection(adapter.getPosition(getItemRecipeText(recipe)));
			else
				spRecipe.setSelection(0);


			if (recipeMap.size() > 0 && recipeMap.containsKey(spRecipe.getItemAtPosition(spRecipe.getSelectedItemPosition())))
				recipeId = recipeMap.get(spRecipe.getItemAtPosition(spRecipe.getSelectedItemPosition()).toString());

			spRecipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					try {
						if (position == 0){
							recipePrice = 0;
						}
						if (recipeMap != null) {
							recipeId = recipeMap.get(parent.getItemAtPosition(position).toString());
							Recipe r = recipeRepo.getRecipe(recipeId);
							currentRecipe = r;

							if (r != null && !firstEntry) {
								//удаляем из базы предсохраненные при загрузке заказа строки рецепта
								for (RecipeLine rl : recipeLines) {
									if (rl.getBookingId() == bookingId && bookingId != 0 || !rl.isDefault())
										bookingRepo.delete(DBHelper.TABLE_RECIPE_LINES, rl.getId());
								}
								recipeLines.clear();
								updateGridViewRecipeLines(RecipeLineLoading.LOAD_TEMPLATE_COMPONENTS);
							} else if (firstEntry)
								firstEntry = false;

						}
						if (currentRecipe != null)
							chooseWeightOrCount(currentRecipe.isCountable());
						//при изменении рецепта, поле себестоимость должна пересчитываться
						if (weight > 0) {
							calculateSummary();
						}
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "Что то пошло не так в spRecipe.setOnItemSelectedListener", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					Toast.makeText(getApplicationContext(), "Вы не выбрали рецепт!", Toast.LENGTH_SHORT)
							.show();
				}
			});
		}
		catch (Exception e){
			Toast.makeText(getApplicationContext(), "Не удалось загрузить рецепты", Toast.LENGTH_SHORT).show();
			recipeId = -1;
		}
	}

	/**
	 * выпадающий список заказчиков
	 */
	private void updateSpinnerBookingMan(BookingMan currentBookingMan) {
		try {
			ArrayList<BookingMan> bmen = bmRepo.getAllBookingMen();
			final List<Map<String, String>> items = new ArrayList<Map<String, String>>();
			bMenMap = new HashMap<String, Long>();
			bMenIds = new HashMap<Integer, Long>();

			int i = 0;
			ArrayList<BookingMan> bmenWithEmptyItem = new ArrayList<BookingMan>();
			bmenWithEmptyItem.add(i, new BookingMan(DEFAULT_SPINNER_RECORD, "", ""));

			bMenMap.put(DEFAULT_SPINNER_RECORD, Long.valueOf(-1));
			bMenIds.put(i, Long.valueOf(-1));
			i++;

			for (BookingMan bm : bmen) {
				bmenWithEmptyItem.add(bm);
				bMenMap.put(bm.getNameWithPhone(), bm.getId());
				bMenIds.put(i, bm.getId());
				i++;
			}

			if (bMenIds.size() > 0 && bookingManId == -1)
				bookingManId = bMenIds.get(0);

			SpinnerSubtextAdapter adapter = new SpinnerSubtextAdapter(this, bmenWithEmptyItem);

			spBookingMan = (Spinner) findViewById(R.id.spinnerChooseBookingMan);
			spBookingMan.setAdapter(adapter);
			spBookingMan.setPrompt("Заказчик");

			if (currentBookingMan != null) {
				spBookingMan.setSelection(adapter.getPosition(currentBookingMan));
			} else {
				spBookingMan.setSelection(0);
			}

			int p = spBookingMan.getSelectedItemPosition();

			if (bMenMap.size() > 0 && bMenMap.containsKey(spBookingMan.getItemAtPosition(spBookingMan.getSelectedItemPosition()))
					&& bookingManId == -1)
				bookingManId = bMenMap.get(spBookingMan.getItemAtPosition(spBookingMan.getSelectedItemPosition()).toString());

			spBookingMan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					try {
						if (bMenIds.containsKey(position)) {
							bookingManId = bMenIds.get(position);
						}
					}
					catch(Exception e){
						Toast.makeText(getApplicationContext(), "Что то пошло не так в spBookingMan.setOnItemSelectedListener", Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					bookingManId = -1;
				}
			});
		}
		catch (Exception e){
			Toast.makeText(getApplicationContext(), "Не удалось загрузить заказчиков", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * выпадающий список типов заказов
	 */
	private void updateBookingTypes() {
		try{
			ArrayList<BookingType> bTypes = btRepo.getAllBookingTypes();

			BookingTypeGridViewAdapter adapter = new BookingTypeGridViewAdapter(this, bTypes, bookingTypeId);
			gvBookingTypes.setAdapter(adapter);
			gvBookingTypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
					bookingTypeId = id;
					if (currentRecipe != null)
						chooseWeightOrCount(currentRecipe.isCountable());
					updateBookingTypes();
				}
			});
		}
		catch (Exception e){
			Toast.makeText(getApplicationContext(), "Не удалось загрузить типы заказов", Toast.LENGTH_SHORT).show();
		}
	}

	private void chooseWeightOrCount(boolean isCountable){
		if (isCountable){
			etCountProduct.setVisibility(View.VISIBLE);
			etWeight.setVisibility(View.INVISIBLE);
			etCakePrice.setHint(PER_COUNT);
		}
		else{
			etCountProduct.setVisibility(View.INVISIBLE);
			etWeight.setVisibility(View.VISIBLE);
			etCakePrice.setHint(IN_ROUBLE);
		}
	}

	private String GetSpinnerItemValueForEvents(Event ev){
		BookingMan bm = bmRepo.getBookingMan(ev.getBookingManId());
		return StringExtension.makeShortString(String.format("%s %s %s %s",
				!ev.getName().equals("") ? ev.getName() : "",
				!ev.getPlace().equals("") ? ", " + ev.getPlace() : "",
				ev.getDateLong() != 0 ? ", " + DateExtension.getDate(ev.getDateLong()) : "",
				bm != null ? ", " + bm.getNameWithPhone() : ""
		), MAX_SPINNER_EVENTS_ITEM_LENGTH);
	}

	/**
	 * заполнить событие заказа
	 */
	private void SetEvent(long eventId){
		if (eventId > 0){
			this.eventId = eventId;
			Event e = eventRepo.getEvent(eventId);
			if (e != null){
				tvEvent.setText(GetSpinnerItemValueForEvents(e));
			}
			else{
				tvEvent.setText(Event.EMPTY_EVENT);
			}
		}
		else{
			tvEvent.setText(Event.EMPTY_EVENT);
		}
	}
	/**
	 * выпадающий список событий
	 */
	/*
	private void updateSpinnerEvents(long eventId) {

		ArrayList<Event> events = bookingRepo.getAllEvents();
		ArrayList<String> sEvents = new ArrayList<String>();
		eventsMap = new HashMap<String, Long>();
		eventsIds = new HashMap<Integer, Long>();
		int i = 0;
		for (Event ev : events) {
			//теоретически возможна ситуация, когда будет событие с одним местом, датой, названием,
			// поэтому добавить ИД к строке
			String value = getSpinnerItemValueForEvents(ev);
			sEvents.add(value);
			eventsMap.put(value, ev.getId());
			eventsIds.put(i, ev.getId());
			i++;
		}
		// адаптер
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sEvents);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spEvent = (Spinner) findViewById(R.id.spinnerChooseEvent);
		spEvent.setAdapter(adapter);
		spEvent.setPrompt("Событие");
		if (eventId != -1) {
			Event e = bookingRepo.getEvent(eventId);
			if (e != null)
				spEvent.setSelection(adapter.getPosition(getSpinnerItemValueForEvents(e)));
			else
				spEvent.setSelection(0);
		}
		else
			spEvent.setSelection(0);


		if (eventsMap.size() > 0 && eventsMap.containsKey(spEvent.getItemAtPosition(spEvent.getSelectedItemPosition())))
			eventId = eventsMap.get(spEvent.getItemAtPosition(spEvent.getSelectedItemPosition()).toString());
	}
	*/
	/**
	 * обработчик нажатия на кнопку
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		    case R.id.buttonSaveBooking: {
				SaveBooking();
			}
			break;
			case R.id.btnAddBookingMan:{
				try {
					showAddBookingManDialog();
				}
				catch(Exception e){
					Toast.makeText(this, "При добавлении заказчика возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
			} break;
			case R.id.buttonAddComponent:{
				try {
					showAddOrChooseRecipeLineDialog(-1);
				}
				catch(Exception e){
					Toast.makeText(this, "При добавлении ингридиента возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
			} break;
			case R.id.btnAddEvent:{
				try {
					intent = new Intent(this, AddOrChooseEvent.class);
					intent.setData(Uri.parse("eventId:" + eventId));
					startActivityForResult(intent, 1);
				}
				catch(Exception e){
					Toast.makeText(this, "При добавлении типа заказа возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
			case R.id.btnAddRecipe:{
				try{
					intent = new Intent(this, AddRecipe.class);
					intent.setData(Uri.parse("createNewRecipeFromBooking:" + true));
					startActivityForResult(intent, 1);
				}
				catch(Exception e){
					Toast.makeText(this, "При добавлении рецепта возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
		}	
	}

	/**
	 * получение значения из дочерних активити
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		if (data.getData() != null) {
			if (data.getData().getScheme().equals("bookingManId")) {
				bookingManId = Long.parseLong(data.getData().getSchemeSpecificPart());
				updateSpinnerBookingMan(bmRepo.getBookingMan(bookingManId));
			} else if (data.getData().getScheme().equals("recipeId")) {
				recipeId = Long.parseLong(data.getData().getSchemeSpecificPart());
				updateSpinnerRecipes(recipeRepo.getRecipe(recipeId));
			} else if (data.getData().getScheme().equals("bookingTypeId")) {
				bookingTypeId = Long.parseLong(data.getData().getSchemeSpecificPart());
				updateBookingTypes();
			} else if (data.getData().getScheme().equals("eventId")) {
				eventId = Long.parseLong(data.getData().getSchemeSpecificPart());
				SetEvent(eventId);
			}
		}
		updateGridViewRecipeLines(RecipeLineLoading.LOAD_ALL_COMPONENTS);
	}

	/**
	 * Сохранение заказа
	 */
	private void SaveBooking(){

		double cakePrice = 0;
		double recipePrice = 0;
		double weight = 0;
		double discount = 0;
		int countProduct = 0;
		int timeSpent = sbTimeSpent.getProgress();

		try {
			if (etCakePrice.getText().toString().length() > 0) {
				try {
					cakePrice = nf.parse(etCakePrice.getText().toString()).doubleValue();
				} catch (Exception e){
					cakePrice = 0;
				}
			}
			if (tvRecipePrice.getText().toString().length() > 0){
				try {
					recipePrice = nf.parse(tvRecipePrice.getText().toString()).doubleValue();
				}
				catch (Exception e) {
					recipePrice = 0;
				}
			}
			if (etWeight.getText().toString().length() > 0) {
				try {
					weight = nf.parse(etWeight.getText().toString()).doubleValue();
				} catch (Exception e) {
					weight = 0;
				}
			}
			if (etDiscount.getText().toString().length() > 0) {
				try {
					discount = nf.parse(etDiscount.getText().toString()).doubleValue();
				} catch (Exception e) {
					discount = 0;
				}
			}
			if (etCountProduct.getText().toString().length() > 0) {
				try {
					countProduct = nf.parse(etCountProduct.getText().toString()).intValue();
				} catch (Exception e) {
					countProduct = 0;
				}
			}
			Date savedDate = Calendar.getInstance().getTime();
			Booking booking = new Booking(bookingId,
					DateExtension.getDate(tvDate.getText().toString()).getTime() + DateExtension.getTime(tvTime.getText().toString()),
					recipeId,
					bookingManId,
					etComment.getText().toString(),
					cakePrice,
					recipePrice,
					weight,
					discount,
					eventId,
					bookingTypeId,
					countProduct,
					timeSpent
					);
			booking.setUpdateDate(savedDate.getTime());
			bookingRepo.update(DBHelper.TABLE_BOOKINGS, booking.getInsertedColumns(), bookingId);

			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
			Toast.makeText(this, "Заказ сохранен", Toast.LENGTH_SHORT)
					.show();
		}
		catch(Exception e){
			Toast.makeText(this, "При сохранении заказа возникла ошибка", Toast.LENGTH_SHORT)
					.show();
		}
	}

	/**
	 * создает записи строк рецепта для текущего заказа
	 */
	private void createRecipeLinesForBooking(ArrayList<RecipeLine> rLines, boolean isLoadTemplate){
		//сначала удалим те, которые уже создавались
		for(RecipeLine rl : recipeLines) {
			if (rl != null){
				for(int i = 0; i < rLines.size(); i++) {
					if (rl.getId() == rLines.get(i).getId()) {
						rl.setWeight(rLines.get(i).getWeight());
						rl.setCount(rLines.get(i).getCount());
						rLines.remove(i);
						i--;
					}
				}
			}
		}

		for (RecipeLine rl : rLines) {
			if (rl != null) {
				//создаем дубликат шаблонной записи, чтобы потом изменять строки рецепта у заказа, а не у шаблона заказа
				if (rl.isDefault() && rl.getBookingId() == Booking.DEFAULT_BOOKING_ID) {
					RecipeLine newRL = new RecipeLine(rl.getWeight(), rl.getCount(), rl.getComponentId(),
							rl.getRecipeId(), bookingId, rl.getState());
					long newRLId = bookingRepo.insert(dbHelper.TABLE_RECIPE_LINES, newRL.getInsertedColumns());
					recipeLines.add(rlRepo.getRecipeLine(newRLId));
				}
				//добавленную пользователем строку рецепта уже сохраняли ранее, поэтому можно просто добавить ссылку
				else
					recipeLines.add(rl);
			}
		}
	}

	/**
	 * Обновление списка строк рецепта
	 */
	private void updateGridViewRecipeLines(RecipeLineLoading variant) {
		try{
			ArrayList<RecipeLine> rLines = null;
			//если изменили шаблон, то загружаем только ингридиенты шаблона
			if (variant.equals(RecipeLineLoading.LOAD_TEMPLATE_COMPONENTS)){
				rLines = rlRepo.getRecipeLines(recipeId);
				//пересоздаем ингидиенты для конкретного заказа
				createRecipeLinesForBooking(rLines, true);
			}
			//иначе загружаем ингридиенты заказа
			else
			{
				rLines = rlRepo.getAllRecipeLinesArrayListByBooking(bookingId);
				//пересоздаем ингидиенты для конкретного заказа
				createRecipeLinesForBooking(rLines, false);
			}
			recipeLinesIds = new HashMap<Integer, Long>();
			for (int i = 0; i < recipeLines.size(); i++) {
				recipeLinesIds.put(i, recipeLines.get(i).getId());
			}

			if(recipeLines.size() % 2 != 0) {
				ArrayList<RecipeLine>  evenRecipeLines = new ArrayList<RecipeLine>();
				for(RecipeLine rl : recipeLines) {
					evenRecipeLines.add(rl);
				}
				evenRecipeLines.add(null);
				bcAdapter = new BookingComponentAdapter(this, R.layout.item_recipe_component, evenRecipeLines, compRepo);
			}
			else {
				bcAdapter = new BookingComponentAdapter(this, R.layout.item_recipe_component, recipeLines, compRepo);
			}

			gridViewAddedComponents.setAdapter(bcAdapter);
			registerForContextMenu(gridViewAddedComponents);
			GridViewExtension.setGridViewHeightBasedOnChildren(this, gridViewAddedComponents, 2);
		}
		catch (Exception e){
			Toast.makeText(getApplicationContext(), "Не удалось загрузить строки рецепта", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * создание контестного меню
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenu.ContextMenuInfo menuInfo) {
		switch (v.getId()) {
			case R.id.gridViewAddedComponents:
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
					acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
					long id = -1;
					try {
						id = recipeLinesIds.get((int) acmi.id);
					}
					finally {
						showAddOrChooseRecipeLineDialog(id);
					}
				}
				catch(Exception e)
				{
					Toast.makeText(this, "При обновлении ингридиента возникла ошибка", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case MENU_DELETE:
				try {
					acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
					if (recipeLinesIds.get((int) acmi.id) == null){
						Toast.makeText(this, "Нечего удалять", Toast.LENGTH_SHORT)
								.show();
					}
					else {
						RecipeLine rl = rlRepo.getRecipeLine(recipeLinesIds.get((int) acmi.id));
						bookingRepo.delete(DBHelper.TABLE_RECIPE_LINES, rl.getId());
						for (int i = 0; i < recipeLines.size(); i++) {
							if (recipeLines.get(i) != null) {
								if (recipeLines.get(i).getId() == rl.getId()) {
									recipeLines.remove(i);
									break;
								}
							}
						}
						updateGridViewRecipeLines(RecipeLineLoading.LOAD_ALL_COMPONENTS);
						Toast.makeText(this, "Ингридиент удален из заказа", Toast.LENGTH_SHORT)
								.show();
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

	public void onclickDialogDate(View view) {
      showDialog(DIALOG_DATE);
    }
	public void onclickDialogTime(View view) {
      showDialog(DIALOG_TIME);
  }

	/**
	 * заполнение дата- и тайм-пикеров
	 */
	protected Dialog onCreateDialog(int id) {
      if (id == DIALOG_DATE) {
	    Calendar c = Calendar.getInstance(); 
        return new DatePickerDialog(this, myCallBackDate,
				c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
      }
      else if (id == DIALOG_TIME) {
	    Calendar c = Calendar.getInstance(); 
    	return new TimePickerDialog(this, myCallBackTime,
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
      }
      return super.onCreateDialog(id);
    }

	/**
	 * формирование даты
	 */
	private OnDateSetListener myCallBackDate = new OnDateSetListener() {
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
	        int dayOfMonth) {
    	  monthOfYear = monthOfYear + 1;
	      tvDate.setText(String.format("%s.%s.%s",
				  dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth,
				  monthOfYear < 10 ? "0" + monthOfYear : monthOfYear,
				  year));
	    }
    };

	/**
	 * формирование времени
	 */
	private OnTimeSetListener myCallBackTime = new OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
          tvTime.setText(String.format("%s:%s",
				  hourOfDay < 10 ? "0" + hourOfDay : hourOfDay,
				  minutes < 10 ? "0" + minutes : minutes));
 		       }
	};

	/**
	 * освобождение неиспользуемых ресурсов
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (bookingRepo != null)
			bookingRepo.close();
	}

	/**
	 * Подтверждение выхода
	 */
	@Override
	public void onBackPressed() {
		backConfirmation();
		super.onBackPressed();
	}

	/**
	 * Подтверждение выхода
	 * */
	void backConfirmation(){
		//мы находились в создании заказа или в его редактировании
		if (isCreateState) {
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle("Отменить создание заказа")
					.setMessage("Вы уверены, что хотите прервать создание заказа? Введенные данные не будут сохранены!")
					.setPositiveButton("Да", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//удаляем несохраненную запись заказа
							bookingRepo.delete(dbHelper.TABLE_BOOKINGS, bookingId);
							finish();
						}

					})
					.setNegativeButton("Нет", null)
					.show();
		}
		else
		{
			//SaveBooking();
		}
	}

	public void showAddOrChooseRecipeLineDialog(long recipeLineId) {
		DialogFragment dialog = AddRecipeLineDialog.newInstance(bookingId, recipeLineId, -1);
		dialog.show(getSupportFragmentManager(), "AddRecipeLineDialog");
	}

	@Override
	public void onRecipeLineDialogPositiveClick(DialogFragment dialog, long recipeLineId, long recipeId) {
		updateGridViewRecipeLines(RecipeLineLoading.LOAD_ALL_COMPONENTS);
	}

	@Override
	public void onRecipeLineDialogNegativeClick(DialogFragment dialog) {
	}

	public void showAddBookingManDialog() {
		DialogFragment dialog = AddBookingManDialog.newInstance();
		dialog.show(getSupportFragmentManager(), "AddBookingManDialog");
	}

	@Override
	public void onBookingManDialogPositiveClick(DialogFragment dialog, long bookingManId) {
		this.bookingManId = bookingManId;
		updateSpinnerBookingMan(bmRepo.getBookingMan(bookingManId));
	}

	@Override
	public void onBookingManDialogNegativeClick(DialogFragment dialog) {

	}
}
