export enum TaskStatus {
    PENDING = 'PENDING',
    IN_PROGRESS = 'IN_PROGRESS',
    COMPLETED = 'COMPLETED'
}

export interface Task {
    id: string;
    title: string;
    description: string;
    createdAt: string;
    status: TaskStatus;
}

export interface PaginatedResponse<T>{
    content: T[];
    totalElements: number;
    totalPages: number;
    currentPage: number;
    pageSize: number;
}

export type TaskRequest = Omit<Task, 'id' | 'createdAt'>;