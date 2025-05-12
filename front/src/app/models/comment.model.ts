export interface Comment {
  id?       : number;
  content   : string;
  createdAt?: Date;
  username  : string;
  userId    : number;
  articleId : number;
}