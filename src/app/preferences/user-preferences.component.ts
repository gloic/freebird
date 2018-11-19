import {Component, Inject, OnInit} from '@angular/core';
import {UserPreferences} from './shared/user-preferences';
import {UserPreferencesService} from './shared/user-preferences.service';
import {SelectItem} from 'primeng/components/common/api';
import {Message} from 'primeng/primeng';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from "../guard/authentication.service";

@Component({
  moduleId: module.id,
  selector: 'user-preferences',
  templateUrl: 'user-preferences.component.html'
})
export class UserPreferencesComponent implements OnInit {
  userPreferences: UserPreferences;
  msgs: Message[] = [];

  languagesPossiblesValues: SelectItem[];
  qualitiesPossiblesValues: SelectItem[];

  userForm: FormGroup;

  constructor(private userPreferenceService: UserPreferencesService, @Inject(FormBuilder) private fb: FormBuilder, private authenticationService: AuthenticationService) {
  }

  ngOnInit() {

    this.userForm = this.fb.group({
      passwordCurrent: this.fb.control('', Validators.required),
      passwordNew1: this.fb.control('', Validators.required),
      passwordNew2: this.fb.control('', Validators.required)
    }, {validator: this.matchingPasswords('passwordNew1', 'passwordNew2')});

    this.userPreferenceService.getPreferences().subscribe(p => {
      this.userPreferences = p;
      this.languagesPossiblesValues = [];
      for (let i = 0; i < this.userPreferences.languagesPossiblesValues.length; i++) {
        this.languagesPossiblesValues.push({
          'label': this.userPreferences.languagesPossiblesValues[i],
          'value': this.userPreferences.languagesPossiblesValues[i]
        });
      }
      this.qualitiesPossiblesValues = [];
      for (let i = 0; i < this.userPreferences.qualitiesPossiblesValues.length; i++) {
        this.qualitiesPossiblesValues.push({
          'label': this.userPreferences.qualitiesPossiblesValues[i].replace('QUALITY_', ''),
          'value': this.userPreferences.qualitiesPossiblesValues[i]
        });
      }
    });
  }

  onPasswordChangeSubmit(): void {
    const currentPassword = this.userForm.controls['passwordCurrent'].value;
    const newPassword = this.userForm.controls['passwordNew2'].value;
    this.userPreferenceService.changePassword(currentPassword, newPassword).subscribe(r => {
      if (r) {
        this.msgs.push({severity: 'info', summary: 'Info Message', detail: 'Your password is changed'});
        this.authenticationService.logout();
      } else {
        this.msgs.push({severity: 'error', summary: 'Error Message', detail: 'Your current password is not correct.'});
        this.userForm.controls['passwordCurrent'].setValue('');
        this.userForm.controls['passwordNew1'].setValue('');
        this.userForm.controls['passwordNew2'].setValue('');
      }
    });
  }

  onPreferencesChangeSubmit(): void {
    const userPreferences = new UserPreferences();
    userPreferences.languagesMovies = this.userPreferences.languagesMovies;
    userPreferences.languagesTvShows = this.userPreferences.languagesTvShows;
    userPreferences.qualities = this.userPreferences.qualities;
    this.userPreferenceService.save(userPreferences).subscribe();
    this.msgs.push({severity: 'info', summary: 'Info Message', detail: 'Preferences saved'});
  }

  matchingPasswords(passwordKey: string, passwordConfirmationKey: string) {
    return (group: FormGroup) => {
      const passwordInput = group.controls[passwordKey];
      const passwordConfirmationInput = group.controls[passwordConfirmationKey];
      if (passwordInput.value !== passwordConfirmationInput.value) {
        return passwordConfirmationInput.setErrors({notEquivalent: true});
      }
    };
  }
}
