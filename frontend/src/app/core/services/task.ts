import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Task, TaskRequest, TaskStatus } from '../models/task.model';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private http = inject(HttpClient);
  private apiUrl = '/api/tasks';

  tasks = signal<Task[]>([]);
  loading = signal<boolean>(false);

  async fetchTasks() {
    this.loading.set(true);
    try {
      const data = await firstValueFrom(this.http.get<Task[]>(this.apiUrl));
      this.tasks.set(data);
    } catch (error) {
      console.error('Error fetching tasks', error);
    } finally {
      this.loading.set(false);
    }
  }

  async addTask(task: TaskRequest) {
    try {
      const res = await firstValueFrom(this.http.post<Task>(this.apiUrl, task));
      this.tasks.update(current => [...current, res]);
    } catch (error) {
      console.error('Error adding task', error);
    }
  }

  async updateTaskStatus(task: Task, newStatus: TaskStatus) {
    const updatedRequest: TaskRequest = {
      title: task.title,
      description: task.description,
      status: newStatus
    };

    try {
      const res = await firstValueFrom(this.http.put<Task>(`${this.apiUrl}/${task.id}`, updatedRequest));
      this.tasks.update(current => current.map(t => t.id === res.id ? res : t));
    } catch (error) {
      console.error('Error al actualizar:', error);
    }
  }

  async updateTask(id: string, task: TaskRequest) {
    try {
      const res = await firstValueFrom(this.http.put<Task>(`${this.apiUrl}/${id}`, task));
      this.tasks.update(current => current.map(t => t.id === id ? res : t));
      return res;
    } catch (error) {
      console.error('Error updating task', error);
      throw error;
    }
  }

  async deleteTask(id: string) {
    try {
      await firstValueFrom(this.http.delete(`${this.apiUrl}/${id}`));
      this.tasks.update(current => current.filter(t => t.id !== id));
    } catch (error) {
      console.error('Error al borrar:', error);
    }
  }

}