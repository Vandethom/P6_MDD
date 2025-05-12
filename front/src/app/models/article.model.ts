import { Theme } from './theme.model';

export interface Article {
  id?       : number;
  title     : string;
  content   : string;
  author?   : string;
  authorId? : number;
  createdAt?: Date;
  updatedAt?: Date;
  imageUrl? : string;
  themes?   : Theme[];
}
