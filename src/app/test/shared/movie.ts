import {LinkDetail} from "../../shared/link/link-detail";
export interface Movie {
  id: number;
  title: string;
  posterPath: string;
  overview: string;
  releaseDate: string;
  url: string;
  size: number;
  links: LinkDetail[];
}
