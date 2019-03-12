
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import domain.CreditCard;
import forms.FormObjectSponsorshipCreditCard;

@Service
@Transactional
public class CreditCardService {

	@Autowired
	private Validator validator;

	public CreditCard reconstruct(FormObjectSponsorshipCreditCard formObject, BindingResult binding) {
		CreditCard card = new CreditCard();

		card.setBrandName(formObject.getBrandName());
		card.setHolderName(formObject.getHolderName());
		card.setNumber(formObject.getNumber());
		card.setExpirationMonth(formObject.getExpirationMonth());
		card.setExpirationYear(formObject.getExpirationYear());
		card.setCvvCode(formObject.getCvvCode());

		// this.validator.validate(card, binding);

		return card;
	}

	public boolean validateCreditCard(CreditCard creditCard) {
		String str = creditCard.getNumber().toString();

		if (Long.toString(creditCard.getNumber()).length() != 16
				|| Integer.toString(creditCard.getExpirationMonth()).length() > 2
				|| Integer.toString(creditCard.getExpirationYear()).length() > 2
				|| Integer.toString(creditCard.getCvvCode()).length() != 3 || creditCard.getExpirationMonth() > 12)
			return false;
		else {
			int[] ints = new int[str.length()];
			for (int i = 0; i < str.length(); i++)
				ints[i] = Integer.parseInt(str.substring(i, i + 1));
			for (int i = ints.length - 2; i >= 0; i = i - 2) {
				int j = ints[i];
				j = j * 2;
				if (j > 9)
					j = j % 10 + 1;
				ints[i] = j;
			}
			int sum = 0;
			for (int i = 0; i < ints.length; i++)
				sum += ints[i];
			if (sum % 10 == 0)
				return true;
			else
				return false;
		}
	}

}
