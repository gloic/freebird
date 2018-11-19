import {LinkLight} from "../../shared/link/link-light";

export interface EpisodeLight {
  id: number;
  episodeNum: number;
  title: string;
  stillPath: string;
  airDate: string;
  links: LinkLight[];
}
