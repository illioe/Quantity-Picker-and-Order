/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 *
 * package com.example.android.justjava;
 */
package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You cannot order more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        display(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "You cannot order less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        display(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        //Find the user's name
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String nameIsField = nameField.getText().toString();

        //Figure out if the customer wants whipped cream topping
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.checkbox_whippedcream);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        //Figure out if the customer wants chocolate topping
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.checkbox_chocolate);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String message = createOrderSummary(nameIsField, price, hasWhippedCream, hasChocolate);
        displayMessage(message);

        if (!(nameIsField.matches(""))) {
            Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
            mailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Coffee order for " + nameIsField);
            mailIntent.putExtra(Intent.EXTRA_TEXT, message);
            if (mailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mailIntent);
            }
        }
    }

    /**
     * Calculates the price of the order.
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not the user wants chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;
        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }
        if (addChocolate) {
            basePrice = basePrice + 2;
        }
        int totalPrice = quantity * basePrice;
        return totalPrice;
    }

    /**
     * This method creates a summary of the order
     * @param nameIsField is the name of the customer
     * @param price
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate is whether or not the user wants chocolate topping
     * @return text summary
     */
    public String createOrderSummary(String nameIsField, int price, boolean hasWhippedCream, boolean hasChocolate) {
        if (nameIsField.matches("")) {
            Toast.makeText(this, "Please, input your name", Toast.LENGTH_SHORT).show();
            return "";
        }
        String message = getString(R.string.order_name) + " " + nameIsField;

        if (hasWhippedCream || hasChocolate) {
            message = message + "\n" + getString(R.string.Toppings) + " : ";
            if (hasWhippedCream) {
                message = message + "\n   - " + getString(R.string.WhippedCream);
            }
            if (hasChocolate) {
                message = message + "\n   - " + getString(R.string.Chocolate);
            }
        }

        message = message + "\n" + getString(R.string.order_quantityOrdered) + " " + quantity;
        message = message + "\n" + getString(R.string.order_Price) + " " + price + "€";
        message = message + "\n" + getString(R.string.ThankYou);
        return message;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.summary_text_view);
        orderSummaryTextView.setText(message);
    }
}