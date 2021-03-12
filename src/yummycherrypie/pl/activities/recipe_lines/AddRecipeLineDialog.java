package yummycherrypie.pl.activities.recipe_lines;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yummycherrypie.base_classes.Booking;
import yummycherrypie.base_classes.Component;
import yummycherrypie.base_classes.Recipe;
import yummycherrypie.base_classes.RecipeLine;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.dal.repositories.RecipeLineRepository;
import yummycherrypie.system.R;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.WeightExtension;
import yummycherrypie.pl.activities.BaseDialogFragmentActivity;
import yummycherrypie.pl.adapters.SpinnerAdapter;

public class AddRecipeLineDialog extends BaseDialogFragmentActivity {

	private final String TITLE = "Добавить ингредиент";

	private IDialogListener mListener;

	private Spinner spComponents;
	private EditText etWeight;

	private DBHelper dbHelper;
	private RecipeLineRepository rlRepo;
	private ComponentRepository compRepo;

	private long bookingId = -1;
	private long recipeLineId = -1;
	private long recipeId = -1;

	private Map<String, Long> componentsMap;
	private Component selectedComponent;

	public interface IDialogListener {
		void onRecipeLineDialogPositiveClick(DialogFragment dialog, long recipeLineId, long recipeId);
		void onRecipeLineDialogNegativeClick(DialogFragment dialog);
	}

	public static AddRecipeLineDialog newInstance(long bookingId, long recipeLineId, long recipeId) {
		AddRecipeLineDialog f = new AddRecipeLineDialog();

		Bundle args = new Bundle();
		args.putLong("bookingId", bookingId);
		args.putLong("recipeLineId", recipeLineId);
		args.putLong("recipeId", recipeId);
		f.setArguments(args);

		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.add_or_choose_recipe_line, null);
		builder.setView(view);
		builder.setTitle(TITLE);

		SpannableString s = new SpannableString(TITLE);
		s.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(getActivity(), StringExtension.DEFAULT_FONT), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		builder.setTitle(s);

		etWeight = (EditText) view.findViewById(R.id.editTextComponentWeight);
		spComponents = (Spinner) view.findViewById(R.id.spinnerComponent);

		dbHelper = new DBHelper(getActivity());
		rlRepo = new RecipeLineRepository(dbHelper);
		compRepo = new ComponentRepository(dbHelper);

		bookingId = getArguments().getLong("bookingId");
		recipeLineId = getArguments().getLong("recipeLineId");
		recipeId = getArguments().getLong("recipeId");

		if (recipeLineId == -1)
		{
			loadComponents("");
		}
		else
		{
			RecipeLine rl = rlRepo.getRecipeLine(recipeLineId);
			etWeight.setText(WeightExtension.getWeight(rl.getWeight()));
			bookingId = rl.getBookingId();
			recipeId = rl.getRecipeId();
			loadComponents(compRepo.getComponentName(rl.getComponentId()));
		}

		builder
				.setPositiveButton("ок", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						try {
							long compId = componentsMap.get(spComponents.getSelectedItem().toString());
							Component component = compRepo.getComponent(compId);
							RecipeLine newRL = new RecipeLine(
									component.isCountable() ? 1 : nf.parse(etWeight.getText().toString()).doubleValue(),
									component.isCountable() ? Integer.valueOf(etWeight.getText().toString()) : 0,
									compId,
									recipeId,
									bookingId,
									bookingId == Booking.DEFAULT_BOOKING_ID && recipeId != Recipe.DEFAULT_RECIPE_ID ? RecipeLine.State.DEFAULT : RecipeLine.State.ADD_BY_USER);
							if (recipeLineId != RecipeLine.DEFAULT_RECIPE_LINE_ID) {
								compRepo.update(DBHelper.TABLE_RECIPE_LINES, newRL.getUpdatedColumns(), recipeLineId);
							}
							else
								recipeLineId = compRepo.insert(DBHelper.TABLE_RECIPE_LINES, newRL.getInsertedColumns());

							mListener.onRecipeLineDialogPositiveClick(AddRecipeLineDialog.this, recipeLineId, recipeId);

						}
						catch(Exception e)
						{
							if (selectedComponent != null) {
								if (selectedComponent.isCountable()) {
									Toast.makeText(getActivity(), "Введите количество ингредиента!", Toast.LENGTH_SHORT)
											.show();
								} else {
									Toast.makeText(getActivity(), "Введите вес ингредиента!", Toast.LENGTH_SHORT)
											.show();
								}
							} else {
								Toast.makeText(getActivity(), "Введите вес ингредиента!", Toast.LENGTH_SHORT)
										.show();
							}
						}
					}
				})
				.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onRecipeLineDialogNegativeClick(AddRecipeLineDialog.this);
						getDialog().dismiss();
					}
				});
		return builder.create();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (IDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement IDialogListener");
		}
	}

	private void loadComponents(String comp) {
		ArrayList<Component> components = compRepo.getAllComponents();
		ArrayList<String> sComponents = new ArrayList<String>();
		componentsMap = new HashMap<String, Long>();
		for (Component c : components) {
			String name = c.getName();
			sComponents.add(name == null ? "Новый ингридиент" : name);
			componentsMap.put(c.getName(), c.getId());
		}
		SpinnerAdapter adapter = new SpinnerAdapter(getActivity(),
				android.R.layout.simple_list_item_1, sComponents);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spComponents.setAdapter(adapter);
		spComponents.setPrompt("Ингридиент");
		spComponents.setSelection(adapter.getPosition(comp));
		spComponents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				long compId = componentsMap.get(spComponents.getSelectedItem().toString());
				selectedComponent = compRepo.getComponent(compId);
				if (selectedComponent.isCountable())
					etWeight.setHint("В штуках");
				else
					etWeight.setHint("В граммах");

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (compRepo != null)
			compRepo.close();
	}
}
