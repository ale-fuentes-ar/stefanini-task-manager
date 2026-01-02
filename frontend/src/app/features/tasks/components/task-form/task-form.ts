import { Component, EventEmitter, inject, Output, signal, input, effect, computed } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { TaskService } from '../../../../core/services/task';
import { Task, TaskStatus } from '../../../../core/models/task.model';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './task-form.html',
  styleUrl: './task-form.scss'
})
export class TaskFormComponent {
  @Output() onSuccess = new EventEmitter<void>();
  @Output() onCancel = new EventEmitter<void>();
  private fb = inject(FormBuilder);
  private taskService = inject(TaskService);
  isSubmitting = signal(false);
  taskToEdit = input<Task | null>(null);
  formTitle = computed(() => this.taskToEdit() ? 'Edit Task' : 'Create New Task');
  formSubtitle = computed(() =>
    this.taskToEdit()
      ? 'Update the details of your personal task below.'
      : 'Add details for your new personal to-do item.'
  );
  submitButtonText = computed(() => this.taskToEdit() ? 'Save Changes' : 'Add Task');

  taskForm = this.fb.group({
    title: ['', [Validators.required, Validators.minLength(3)]],
    description: [''],
    status: [TaskStatus.PENDING, [Validators.required]]
  });

  statusOptions = Object.values(TaskStatus);

  constructor() {
    effect(() => {
      const task = this.taskToEdit();
      if (task) {
        this.taskForm.patchValue({
          title: task.title,
          description: task.description,
          status: task.status
        });
      } else {
        this.taskForm.reset({ status: TaskStatus.PENDING });
      }
    });
  }

  async onSubmit() {
    if (this.taskForm.valid && !this.isSubmitting()) {
      this.isSubmitting.set(true);
      const formValue = this.taskForm.value as any;
      const currentTask = this.taskToEdit();

      try {
        if (currentTask) {
          // MODO EDICIÓN
          await this.taskService.updateTask(currentTask.id, formValue);
        } else {
          // MODO CREACIÓN
          await this.taskService.addTask(formValue);
        }
        this.onSuccess.emit();
      } finally {
        this.isSubmitting.set(false);
      }
    }
  }
}