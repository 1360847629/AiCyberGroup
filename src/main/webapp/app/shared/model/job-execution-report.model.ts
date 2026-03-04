import dayjs from 'dayjs';
import { IJobRequest } from 'app/shared/model/job-request.model';
import { IUser } from 'app/shared/model/user.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IJobExecutionReport {
  id?: number;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  executionNode?: string | null;
  executionLog?: string | null;
  status?: keyof typeof Status;
  jobRequest?: IJobRequest | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IJobExecutionReport> = {};
