package todo.models

case class Task(id: Id, title: String, description: String, userId: Id, completed: Boolean) {
  def withUser(user: User): Task = Task(this.id, this.title, this.description, user.id, completed)
}
