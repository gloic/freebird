export class ApplicationUser {
  username: string;
  password: string;

  id: number;
  enabled: boolean;
  lastPasswordResetDate: number;
  authorities: String[];

}
