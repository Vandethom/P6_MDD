import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export class PasswordValidator {
  /**
   * Validateur de mot de passe selon les spécifications :
   * - ≥ 8 caractères
   * - Au moins 1 chiffre
   * - Au moins 1 lettre minuscule
   * - Au moins 1 lettre majuscule
   * - Au moins 1 caractère spécial
   */
  static strongPassword(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const password = control.value;
      
      if (!password) {
        return null; // Ne pas valider si le champ est vide (laissé à required)
      }

      const errors: ValidationErrors = {};

      // Vérification de la longueur minimum (8 caractères)
      if (password.length < 8) {
        errors['minLength'] = { requiredLength: 8, actualLength: password.length };
      }

      // Vérification de la présence d'au moins un chiffre
      if (!/\d/.test(password)) {
        errors['requiresDigit'] = true;
      }

      // Vérification de la présence d'au moins une lettre minuscule
      if (!/[a-z]/.test(password)) {
        errors['requiresLowercase'] = true;
      }

      // Vérification de la présence d'au moins une lettre majuscule
      if (!/[A-Z]/.test(password)) {
        errors['requiresUppercase'] = true;
      }

      // Vérification de la présence d'au moins un caractère spécial
      if (!/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(password)) {
        errors['requiresSpecialChar'] = true;
      }

      return Object.keys(errors).length === 0 ? null : errors;
    };
  }

  /**
   * Génère un message d'erreur lisible pour l'utilisateur
   */
  static getErrorMessage(errors: ValidationErrors): string {
    if (errors['minLength']) {
      return `Le mot de passe doit contenir au moins 8 caractères (${errors['minLength'].actualLength}/8)`;
    }
    if (errors['requiresDigit']) {
      return 'Le mot de passe doit contenir au moins un chiffre';
    }
    if (errors['requiresLowercase']) {
      return 'Le mot de passe doit contenir au moins une lettre minuscule';
    }
    if (errors['requiresUppercase']) {
      return 'Le mot de passe doit contenir au moins une lettre majuscule';
    }
    if (errors['requiresSpecialChar']) {
      return 'Le mot de passe doit contenir au moins un caractère spécial (!@#$%^&*...)';
    }
    return 'Le mot de passe ne respecte pas les critères de sécurité';
  }
}
