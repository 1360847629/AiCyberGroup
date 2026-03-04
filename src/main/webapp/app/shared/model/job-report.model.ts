import dayjs from 'dayjs';
import { IJobRequest } from 'app/shared/model/job-request.model';
import { IUser } from 'app/shared/model/user.model';

export interface IJobReport {
  id?: number;
  reportDate?: dayjs.Dayjs;
  details?: string | null;
  isSuccessful?: boolean | null;
  jobRequest?: IJobRequest | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IJobReport> = {
  isSuccessful: false,
};
