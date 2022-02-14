package util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeUtil {
    /**
     * 自下而上遍历
     *
     * @param parent      父节点 ，当为根节点时为空
     * @param node        根
     * @param getChildren 获取子节点的方法
     * @param handler     方法
     * @param <T>         t
     */
    public static <T> void bottomup(T parent, T node, Function<T, List<T>> getChildren, BiConsumer<T, T> handler) {
        List<T> children = getChildren.apply(node);

        if (children != null && children.size() > 0) {
            // 中间节点，先处理子节点
            for (T child : children) {
                TreeUtil.bottomup(node, child, getChildren, handler);
            }
            // 处理完子节点处理父级节点，直到parent=null
            handler.accept(parent, node);
        } else {
            // 叶子节点，最先处理
            handler.accept(parent, node);
        }
    }

    /**
     * 自下而上遍历
     *
     * @param parent      父节点 ，当为根节点时为空
     * @param node        根
     * @param getChildren 获取子节点的方法
     * @param handler     方法
     * @param comparator  比较方法
     * @param <T>         t
     */
    public static <T> void bottomup(T parent, T node, Function<T, List<T>> getChildren, BiConsumer<T, T> handler, Comparator comparator) {
        List<T> children = getChildren.apply(node);

        if (children != null && children.size() > 0) {
            if (null != comparator) {
                children.sort(comparator);
            }
            // 中间节点，先处理子节点
            for (T child : children) {
                TreeUtil.bottomup(node, child, getChildren, handler);
            }
            // 处理完子节点处理父级节点，直到parent=null
            handler.accept(parent, node);
        } else {
            // 叶子节点，最先处理
            handler.accept(parent, node);
        }
    }

    /**
     * @param rootList
     * @param getChildren
     * @param handler
     * @param <T>
     * @see TreeUtil#bottomup(Object, Object, Function, BiConsumer)
     */
    public static <T> void bottomup(List<T> rootList, Function<T, List<T>> getChildren, BiConsumer<T, T> handler) {
        for (T root : rootList) {
            bottomup(null, root, getChildren, handler);
        }
    }

    /**
     * 自上至下遍历
     *
     * @param root        根
     * @param getChildren 获取子节点的方法
     * @param handler     方法
     * @param <T>         t
     */
    public static <T> void topdown(T root, Function<T, List<T>> getChildren, BiConsumer<T, T> handler) {
        List<T> children = getChildren.apply(root);
        if (children != null && children.size() > 0) {
            for (T child : children) {
                handler.accept(root, child);
                TreeUtil.topdown(child, getChildren, handler);
            }
        }
    }

    public static <T> void topdown(List<T> root, Function<T, List<T>> getChildren, BiConsumer<T, T> handler) {
        for (T child : root) {
            handler.accept(null, (T) child);
            TreeUtil.topdown((T) child, getChildren, handler);
        }
    }

    public static <T> void listTree(List<T> root, Function<T, List<T>> getChildren, Function<T, String> toString) {
        topdown(root, getChildren, (parent, child) -> {
            toString.apply(child);
        });
    }

    public static <T, E> List<T> buildTree(List<T> flatList, E rootId, Function<T, E> parentIdGetter, Function<T, E> idGetter,
        BiConsumer<T, List<T>> childrenSetter, Comparator<T> comparator) {
        Map<E, List<T>> groupByParentId = flatList.stream()
            .collect(Collectors.groupingBy(t -> Optional.ofNullable(parentIdGetter.apply(t)).orElse(rootId), Collectors.toList()));
        List<T> ts = groupByParentId.get(rootId);
        List<T> result = new ArrayList<>();
        if (null != ts && ts.size() > 0) {
            if (null != comparator) {
                ts.sort(comparator);
            }
            ts.forEach(t -> {
                buildTree(t, idGetter, childrenSetter, groupByParentId, comparator);
                result.add(t);
            });
        }
        return result;
    }

    public static <T, E> void buildTree(T root, Function<T, E> idGetter, BiConsumer<T, List<T>> childrenSetter,
        Map<E, List<T>> groupByParentId, Comparator<T> comparator) {
        List<T> children = groupByParentId.get(idGetter.apply(root));
        if (null != children && children.size() > 0) {
            if (null != comparator) {
                children.sort(comparator);
            }
            childrenSetter.accept(root, children);
            for (T child : children) {
                // recursively
                buildTree(child, idGetter, childrenSetter, groupByParentId, comparator);
            }
        }
    }

}
