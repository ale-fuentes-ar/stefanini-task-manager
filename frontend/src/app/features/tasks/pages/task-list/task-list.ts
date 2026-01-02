import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { Task, TaskStatus } from '../../../../core/models/task.model';
import { TaskService } from '../../../../core/services/task';
import { TaskFormComponent } from '../../components/task-form/task-form';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [TaskFormComponent],
  templateUrl: './task-list.html',
  styleUrl: './task-list.scss'
})
export class TaskListComponent implements OnInit {
  protected taskService = inject(TaskService);
  protected readonly TaskStatus = TaskStatus;
  showModal = signal(false);
  filterStatus = signal<string>('ALL');
  taskToEdit = signal<Task | null>(null);

  totalTasks = computed(() => this.taskService.tasks().length);
  pendingTasks = computed(() => this.taskService.tasks().filter(t => t.status === TaskStatus.PENDING).length);
  progressTasks = computed(() => this.taskService.tasks().filter(t => t.status === TaskStatus.IN_PROGRESS).length);
  completedTasks = computed(() => this.taskService.tasks().filter(t => t.status === TaskStatus.COMPLETED).length);

  filteredTasks = computed(() => {
    const tasks = this.taskService.tasks();
    const filter = this.filterStatus();

    if (filter === 'ALL') return tasks;
    return tasks.filter(t => t.status === filter);
  });


  ngOnInit() {
    this.taskService.fetchTasks();
  }

  getStatusClass(status: TaskStatus) {
    return {
      'pending': status === TaskStatus.PENDING,
      'progress': status === TaskStatus.IN_PROGRESS,
      'completed': status === TaskStatus.COMPLETED
    };
  }

  getBadgeClass(status: TaskStatus) {
    switch (status) {
      case TaskStatus.PENDING: return 'bg-yellow-100 text-yellow-700 border-yellow-200';
      case TaskStatus.IN_PROGRESS: return 'bg-blue-100 text-blue-700 border-blue-200';
      case TaskStatus.COMPLETED: return 'bg-green-100 text-green-700 border-green-200';
      default: return 'bg-gray-100 text-gray-700';
    }
  }

  openCreateModal() {
    this.taskToEdit.set(null); // Limpiamos para creación
    this.showModal.set(true);
  }

  openEditModal(task: Task) {
    this.taskToEdit.set(task); // Cargamos la tarea para editar
    this.showModal.set(true);
  }

}