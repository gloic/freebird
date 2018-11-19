import {EpisodeLight} from "./episode-light";
import {SeasonLight} from "./season-light";
import {TvShowLight} from "./tvshow-light";
import {LinkDetail} from "../../shared/link/link-detail";

export interface EpisodeDetail extends EpisodeLight {
  size: number;
  overview: string;
  season: SeasonLight;
  tvShow: TvShowLight;
  links: LinkDetail[];
}
