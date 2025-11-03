package Validator;

import Exception.PaymentDetailsException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BICValidator extends StringValidator {

    private static final String BIC_REGEX = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$";
    private static final Pattern BIC_PATTERN = Pattern.compile(BIC_REGEX);

    @Override
    public void validate(Object obj) throws Exception {
        super.validate(obj);
        if (isValid()) {
            String bic = (String) obj;
            Matcher matcher = BIC_PATTERN.matcher(bic);
            if (!matcher.matches()) {
                String msg = "Eingabe ist kein gültiger BIC.";
                errors.add(msg);
                throw new PaymentDetailsException(msg);
            }
        }
    }
}



//
//BICValidator bicValidator = new BICValidator();
//
//String testBic = "DEUTDEFF"; // Beispiel BIC
//bicValidator.validate(testBic);
//
//if (!bicValidator.isValid()) {
//    System.out.println("Fehler beim BIC:");
//    for (String error : bicValidator.getErrors()) {
//        System.out.println(error);
//    }
//    bicValidator.saveErrorsToCsv();
//} else {
//    System.out.println("BIC ist gültig.");
//}
