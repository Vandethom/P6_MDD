package com.openclassrooms.mddapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    // Pattern pour vérifier la complexité du mot de passe
    private static final Pattern HAS_DIGIT = Pattern.compile("\\d");
    private static final Pattern HAS_LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern HAS_UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern HAS_SPECIAL_CHAR = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");

    @Override
    public void initialize(StrongPassword constraintAnnotation) {
        // Rien à initialiser
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            return true; // Laissé à @NotBlank pour valider la présence
        }

        boolean isValid = true;
        context.disableDefaultConstraintViolation();

        // Vérifier la longueur minimum (8 caractères)
        if (password.length() < 8) {
            context.buildConstraintViolationWithTemplate(
                "Le mot de passe doit contenir au moins 8 caractères"
            ).addConstraintViolation();
            isValid = false;
        }

        // Vérifier la présence d'au moins un chiffre
        if (!HAS_DIGIT.matcher(password).find()) {
            context.buildConstraintViolationWithTemplate(
                "Le mot de passe doit contenir au moins un chiffre"
            ).addConstraintViolation();
            isValid = false;
        }

        // Vérifier la présence d'au moins une lettre minuscule
        if (!HAS_LOWERCASE.matcher(password).find()) {
            context.buildConstraintViolationWithTemplate(
                "Le mot de passe doit contenir au moins une lettre minuscule"
            ).addConstraintViolation();
            isValid = false;
        }

        // Vérifier la présence d'au moins une lettre majuscule
        if (!HAS_UPPERCASE.matcher(password).find()) {
            context.buildConstraintViolationWithTemplate(
                "Le mot de passe doit contenir au moins une lettre majuscule"
            ).addConstraintViolation();
            isValid = false;
        }

        // Vérifier la présence d'au moins un caractère spécial
        if (!HAS_SPECIAL_CHAR.matcher(password).find()) {
            context.buildConstraintViolationWithTemplate(
                "Le mot de passe doit contenir au moins un caractère spécial (!@#$%^&*...)"
            ).addConstraintViolation();
            isValid = false;
        }

        return isValid;
    }
}
