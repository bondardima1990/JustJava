package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view){
        // Get user's name
        EditText nameField = findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        // Figure out if the user wants whipped cream topping
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out if the user wants chocolate topping
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // Calculate the price
        int price = calculatePrice(hasWhippedCream, hasChocolate);

        // Display the order summary on the screen
        String message = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        // Use an intent to launch an email app.
        // Send the order summary in the email body.
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @param addWhippedCream is whether or not we should include whipped cream topping in the price
     * @param addChocolate    is whether or not we should include chocolate topping in the price
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        // First calculate the price of one cup of coffee
        int basePrice = 5;

        // If the user wants whipped cream, add $1 per cup
        if (addWhippedCream)
            basePrice += 1;

        // If the user wants chocolate, add $2 per cup
        if (addChocolate)
            basePrice += 2;

        // Calculate the total order price by multiplying by the quantity
        return quantity * basePrice;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(numberOfCoffees));
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view){
        if (quantity == 100) {
            Toast.makeText(this, R.string.maximum_limit,Toast.LENGTH_SHORT).show();
            return;
        }
        displayQuantity(++quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view){
        if (quantity == 1) {
            Toast.makeText(this, R.string.minimum_limit, Toast.LENGTH_SHORT).show();
            return;
        }
        displayQuantity(--quantity);
    }

    /**
     * Create summary of the order.
     *
     * @param name            on the order
     * @param price           of the order
     * @param addWhippedCream is whether or not to add whipped cream to the coffee
     * @param addChocolate    is whether or not to add chocolate to the coffee
     * @return text summary
     */
    public String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {

        String whippedCream;
        String chocolate;

        if (addWhippedCream) whippedCream = getString(R.string.boolean_true);
        else whippedCream = getString(R.string.boolean_false);

        if (addChocolate) chocolate = getString(R.string.boolean_true);
        else chocolate = getString(R.string.boolean_false);

        return getString(R.string.order_summary_name, name) + '\n' +
                getString(R.string.order_summary_whipped_cream, whippedCream) + '\n' +
                getString(R.string.order_summary_chocolate, chocolate) + '\n' +
                getString(R.string.order_summary_quantity, quantity) + '\n' +
                getString(R.string.order_summary_price, NumberFormat.getCurrencyInstance().format(price)) + '\n' +
                getString(R.string.thank_you);
    }
}