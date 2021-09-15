import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Vegetable> vegetableMap = new HashMap<>();
        // create map of vegetable
        vegetableMap.put("carrot", new Vegetable("carrot", 2.0));
        vegetableMap.put("onion", new Vegetable("onion", 3.0));
        vegetableMap.put("cabbage", new Vegetable("cabbage", 4.0));
        vegetableMap.put("broccoli", new Vegetable("broccoli", 4.0));
        vegetableMap.put("pumpkin", new Vegetable("pumpkin", 5.0));
        vegetableMap.put("sweet potato", new Vegetable("sweet potato", 2.2));

        double total = 0;
        double membershipDiscount = 0;
        double ccCost = 0;
        boolean buyMembership = false;
        boolean isMembership = false;
        boolean isCreditCard = false;
        boolean getBonusDiscount = false;

        try {
            // read the order from excel file in resources folder
            FileInputStream file = new FileInputStream(new File(Main.class.getClassLoader().getResource("order.xlsx").getFile()));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            boolean finish = false;

            isMembership = sheet.getRow(1).getCell(2).getBooleanCellValue();
            buyMembership = sheet.getRow(1).getCell(3).getBooleanCellValue();
            isCreditCard = sheet.getRow(1).getCell(4).getBooleanCellValue();

            // read all order from the excel
            for (int i = 1; !finish; i++) {
                Row row = sheet.getRow(i);
                row.getCell(1);
                String item = row.getCell(0).getStringCellValue();
                Double quantity = row.getCell(1).getNumericCellValue();

                double price = quantity * vegetableMap.get(item).getPrice();
                System.out.printf("You purchase %s with %.0f quantity for %.2f $. The price is : %.2f$\n", item, quantity, vegetableMap.get(item).getPrice(), price);

                total += price;
                if (sheet.getLastRowNum() == i) {
                    finish = true;
                }
            }

            // check if they have membership
            if (isMembership) {
                membershipDiscount = 0.05 * total;
                total *= 0.95;
            } else if (buyMembership) {
                // if don't, ask if they want to buy membership
                // discount then add membership fee
                membershipDiscount = 0.05 * total;
                total *= 0.95;
                total += 100;
            }

            // check if get bonus discount
            if (total > 100) {
                getBonusDiscount = true;
                total -= 10;
            }

            // calculate cc cost
            if (isCreditCard) {
                ccCost = 0.02 * total;
                total *= 1.02;
            }


            if (isMembership) {
                System.out.printf("You use your membership and get 5%% discount with amount: %.2f$\n", membershipDiscount);
            } else if (buyMembership) {
                System.out.printf("you don't have a membership. You buy membership for 100$ and get 5%% discount with amount: %.2f$\n", membershipDiscount);
            }

            if (getBonusDiscount) {
                System.out.println("You get 10$ discount from having bill above 100$");
            }

            if (isCreditCard) {
                System.out.printf("You are charged 2%% credit card fee with amount: %.2f$\n", ccCost);
            }

            System.out.printf("Your final total bill is : %.2f$\n", total);

            file.close();
        } catch (Exception e) {
            System.out.println("something is wrong with the application");
            e.printStackTrace();
        }
    }
}
