package todo.rest.api;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class RestService {

    static List<Todo> todos = null;

    public static void main(String[] args) {

        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        todos = new ArrayList<>();

        after((req, res) -> {
            res.type("application/json");
        });


        path("/todos", () -> {
            /**
             * Default main page.
             */
            get("", (request, response) ->
                    "Nothing todo yet?\nMake a new todo or select a valid todoId!"
            );

            /**
             * List all t o d o's on the same page.
             */
            get("/all", (request, response) -> {
                String allTodos = "";
                int counter = 0;
                for (Todo todo : todos) {
                    allTodos += "Todo number " + counter++ + "\n";
                    allTodos += todo.toJson() + "\n\n";
                }
                return allTodos;
            });

            /**
             * Get a specific t o d o.
             */
            get("/:todoId", (request, response) -> {
                Integer todoId = Integer.parseInt(request.params(":todoId"));
                if (todoId >= 0 && todoId < todos.size()) {
                    return todos.get(todoId).toJson();
                }
                return "Not a valid todoId!";
            });

            /**
             * Create a new t o d o, and show it in the browser.
             */
            post("", (request, response) -> {
                Todo todo = new Gson().fromJson(request.body(), Todo.class);
                todos.add(todo);
                return todos.get(todos.size() - 1).toJson();
            });

            /**
             * Update the specified t o d o.
             */
            put("/:todoId", (request, response) -> {
                Integer todoId = Integer.parseInt(request.params(":todoId"));
                if (todoId >= 0 && todoId < todos.size()) {
                    Todo todo = new Gson().fromJson(request.body(), Todo.class);
                    todos.remove(todoId.intValue());
                    todos.add(todoId, todo);
                    return "Successfully updated todo with id: " + todoId;
                }
                return "Not a valid todoId!";
            });

            /**
             * Delete the specified t o d o.
             */
            delete("/delete/:todoId", (request, response) -> {
                Integer todoId = Integer.parseInt(request.params(":todoId"));
                if (todoId >= 0 && todoId < todos.size()) {
                    todos.remove(todoId.intValue());
                    return "Successfully deleted todo with id: " + todoId;
                }
                return "Not a valid todoId!";
            });
        });
    }
}
