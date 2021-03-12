package yummycherrypie.pl.activities.components;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Date;

import yummycherrypie.base_classes.Component;
import yummycherrypie.dal.DBHelper;
import yummycherrypie.dal.repositories.ComponentRepository;
import yummycherrypie.pl.CurrencyExtension;
import yummycherrypie.pl.StringExtension;
import yummycherrypie.pl.WeightExtension;
import yummycherrypie.pl.activities.BaseDialogActivity;
import yummycherrypie.system.R;

public class AddComponent extends BaseDialogActivity implements OnClickListener {

	private final String TITLE = "Ингредиент";
	private final String TITLE_ADD = "Добавить Ингредиент";

	private DBHelper dbHelper;
	private ComponentRepository dbr;

	private String weightValue = "0";

	private EditText tbComponentName;
	private EditText tbComponentWeight;
	private EditText tbComponentPrice;
	private CheckBox cbCountable;
	private LinearLayout btnComponentSave;

	private long componentId = -1;
	private double oldValuePrice;
	private double oldValueWeight;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_component);

		tbComponentName = (EditText) findViewById(R.id.editTextComponentName);
		tbComponentWeight = (EditText) findViewById(R.id.editTextComponentWeight);
		tbComponentPrice = (EditText) findViewById(R.id.editTextComponentPrice);
		btnComponentSave = (LinearLayout) findViewById(R.id.buttonSaveComponent);
		cbCountable = (CheckBox) findViewById(R.id.checkBoxCountable);

		btnComponentSave.setOnClickListener(this);

		dbHelper = new DBHelper(this);
		dbr = new ComponentRepository(dbHelper);

		cbCountable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					weightValue = tbComponentWeight.getText().toString();
					tbComponentWeight.setText("0");
				} else {
					tbComponentWeight.setText(weightValue);
				}
			}
		});

		tbComponentPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					try {
						oldValuePrice = nf.parse(tbComponentPrice.getText().toString()).doubleValue();
					}
					catch (Exception e){
						oldValuePrice = 0;
					}
					tbComponentPrice.setText("");
				} else {
					if (tbComponentPrice.getText().toString().isEmpty()){
						tbComponentPrice.setText(CurrencyExtension.getCurrency(oldValuePrice));
					}
				}
			}
		});

		tbComponentWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus){
					try {
						oldValueWeight = nf.parse(tbComponentWeight.getText().toString()).doubleValue();
					}
					catch (Exception e){
						oldValueWeight = 0;
					}
					tbComponentWeight.setText("");
				} else {
					if (tbComponentWeight.getText().toString().isEmpty()){
						tbComponentWeight.setText(WeightExtension.getWeight(oldValueWeight));
					}
				}
			}
		});

		componentId = Long.valueOf(getIntent().getData().getSchemeSpecificPart());
		if (componentId == -1)
		{
			Component c = new Component();
			c.setCreateDate(new Date().getTime());
			componentId = dbr.insert(DBHelper.TABLE_COMPONENTS, c.getInsertedColumns());
			setTitle(TITLE_ADD);
		}
		else
		{
			Component c = dbr.getComponent(componentId);
			tbComponentName.setText(c.getName());
			tbComponentWeight.setText(WeightExtension.getWeight(c.getWeight()));
			tbComponentPrice.setText(CurrencyExtension.getCurrencyWithoutRouble(c.getPrice()));
			cbCountable.setChecked(c.isCountable());
			setTitle(TITLE);

		}

		SpannableString s = new SpannableString(getTitle());
		s.setSpan(new yummycherrypie.pl.activities.TypefaceSpan(this, StringExtension.DEFAULT_FONT), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		setTitle(s);

    }

    public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		    case R.id.buttonSaveComponent:
				try {
					Component c = new Component(
							tbComponentName.getText().toString(),
							nf.parse(tbComponentPrice.getText().toString()).doubleValue(),
							nf.parse(tbComponentWeight.getText().toString()).doubleValue(),
							cbCountable.isChecked()
					);
					c.setUpdateDate(new Date().getTime());
					dbr.update(DBHelper.TABLE_COMPONENTS, c.getUpdatedColumns(), componentId);
					intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
					Toast.makeText(this, "Ингридиент сохранен", Toast.LENGTH_SHORT)
							.show();
					//todo пересчитываем все будущие заказы в соответствии с этим ингридиентом
				}
				catch(Exception e)
				{
					Toast.makeText(this, "При сохранении ингридиента возникла ошибка!" + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
		}	
	}

	@Override
    protected void onDestroy() {
  	  super.onDestroy();
  	  if (dbr != null)
  	  	dbr.close();
  	}
}
