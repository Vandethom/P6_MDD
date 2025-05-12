export interface Theme {
  id         : number;
  title      : string;
  description: string;
  createdAt? : string;
}

export interface Subscription {
  id        : number;
  userId    : number;
  themeId   : number;
  theme     : Theme;
  createdAt?: string;
}
